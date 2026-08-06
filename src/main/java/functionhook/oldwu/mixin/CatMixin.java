package functionhook.oldwu.mixin;

import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin {
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
