package functionhook.oldwu.mixin;

import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import functionhook.oldwu.cat.GoodCatLogic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin {
    /** 驯服交互前是否已驯服（用于判断本次交互是否完成驯服）。 */
    @Unique
    private boolean oldwuWasTameBeforeInteract;

    @Inject(method = "<init>", at = @At("HEAD"))
    private static void oldwu_initAccessors(CallbackInfo ci) {
        CatPartners.initAccessors();
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void oldwu_definePartner(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(CatPartners.PARTNER_UUID, CatPartners.NO_PARTNER);
        entityData.define(CatPartners.STATE, CatState.COMMON.ordinal());
        entityData.define(CatPartners.ATTACK_COOLDOWN, 0);
        entityData.define(CatPartners.FLAT_TIMER, 0);
        entityData.define(CatPartners.PAIRING_TIMER, 0);
        entityData.define(CatPartners.DANCE_MODEL_INDEX, 0);
        entityData.define(CatPartners.DANCE_TIMER, 0);
        entityData.define(CatPartners.MAODIE_HAQI_TIMER, 0);
        entityData.define(CatPartners.MAODIE_RAGE_COOLDOWN, 0);
        entityData.define(CatPartners.MAODIE_ANIM_TICK, 0);
        entityData.define(CatPartners.MAODIE_NORMAL_FIRE_COOLDOWN, 0);
        entityData.define(CatPartners.GROOMING_TIMER, 0);
        entityData.define(CatPartners.BATTLE_PEACE_TIMER, 0);
        entityData.define(CatPartners.MIRROR_TICKS, 0);
        entityData.define(CatPartners.GOOD_VALUE, GoodCatLogic.UNASSIGNED);
        entityData.define(CatPartners.HITGROUND_TIMER, 0);
        entityData.define(CatPartners.HITGROUND_COOLDOWN, 0);
        entityData.define(CatPartners.HITGROUND_ANIM_TICK, 0);
        entityData.define(CatPartners.WAS_MAODIE, false);
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void oldwu_mateLogic(CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        if (self.level() instanceof ServerLevel level) {
            CatMatingLogic.tick(level, self);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_savePartner(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        CatPartners.getPartner(self).ifPresent(uuid -> compound.putUUID("oldwu_partner", uuid));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_loadPartner(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        if (compound.hasUUID("oldwu_partner")) {
            CatPartners.setPartner(self, compound.getUUID("oldwu_partner"));
        }
    }

    // 好猫值持久化（-1 未分配不存储，加载后保持 -1 由首次 tick 随机分配）
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_saveGoodValue(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        int value = CatPartners.getGoodValue(self);
        if (value >= 0) {
            compound.putInt("oldwu_good_value", value);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_loadGoodValue(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        if (compound.contains("oldwu_good_value")) {
            CatPartners.setGoodValue(self, compound.getInt("oldwu_good_value"));
        }
    }

    // 曾是 maodie 标记持久化：改名后保留 325 血量
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_saveWasMaodie(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        if (CatPartners.getWasMaodie(self)) {
            compound.putBoolean("oldwu_was_maodie", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void oldwu_loadWasMaodie(CompoundTag compound, CallbackInfo ci) {
        Cat self = (Cat) (Object) this;
        if (compound.getBoolean("oldwu_was_maodie")) {
            CatPartners.setWasMaodie(self, true);
        }
    }

    /**
     * 驯服拦截与好猫值提升：
     * <ul>
     *   <li>好猫值 &lt; 40（坏猫/键帽）无法被驯服——喂生鳕鱼/生鲑鱼时直接返回 PASS（不消耗、不挥臂）。</li>
     *   <li>本次交互完成驯服时好猫值 +10。</li>
     * </ul>
     */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void oldwu_interceptTame(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Cat self = (Cat) (Object) this;
        this.oldwuWasTameBeforeInteract = self.isTame();
        if (self.isTame()) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.COD) && !stack.is(Items.SALMON)) {
            return;
        }
        if (GoodCatLogic.getGoodValue(self) < GoodCatLogic.BAD_THRESHOLD) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "mobInteract", at = @At("TAIL"))
    private void oldwu_applyTameBoost(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Cat self = (Cat) (Object) this;
        if (!this.oldwuWasTameBeforeInteract && self.isTame()) {
            GoodCatLogic.setGoodValue(self, GoodCatLogic.getGoodValue(self) + GoodCatLogic.TAME_BOOST);
        }
    }

    /**
     * 繁殖后代好猫值 = min(100, max(父母好猫值) + 5)。
     */
    @Inject(method = "getBreedOffspring", at = @At("RETURN"))
    private void oldwu_breedGoodValue(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Cat> cir) {
        Cat baby = cir.getReturnValue();
        if (baby != null && otherParent instanceof Cat other) {
            Cat self = (Cat) (Object) this;
            int value = Math.max(GoodCatLogic.getGoodValue(self), GoodCatLogic.getGoodValue(other)) + GoodCatLogic.BREED_BOOST;
            GoodCatLogic.setGoodValue(baby, value);
        }
    }

    private boolean oldwu_isSilent() {
        Cat self = (Cat) (Object) this;
        return CatPartners.getState(self) != CatState.COMMON || CatMatingLogic.isMaodie(self);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void oldwu_silentAmbient(CallbackInfoReturnable<SoundEvent> cir) {
        if (oldwu_isSilent()) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void oldwu_silentHurt(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        if (oldwu_isSilent()) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void oldwu_silentDeath(CallbackInfoReturnable<SoundEvent> cir) {
        if (oldwu_isSilent()) {
            cir.setReturnValue(null);
        }
    }
}
