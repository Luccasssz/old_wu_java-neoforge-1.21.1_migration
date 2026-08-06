package functionhook.oldwu.mixin;

import functionhook.oldwu.advancement.MaodieAdvancements;
import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.MaodieLogic;
import functionhook.oldwu.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "die", at = @At("TAIL"))
    private void oldwu_maodieDrops(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Cat cat) || !CatMatingLogic.isMaodie(cat)) {
            return;
        }

        MaodieLogic.onDeath(cat);
        if (source.getEntity() instanceof ServerPlayer player) {
            MaodieAdvancements.awardDefeatMaodie(player);
        }
        if (!(cat.level() instanceof ServerLevel level)) {
            return;
        }

        int looting = 0;
        if (source.getEntity() instanceof LivingEntity killer) {
            looting = EnchantmentHelper.getEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING),
                    killer);
        }

        int count = 2 + cat.getRandom().nextInt(4);
        for (int i = 0; i < looting; i++) {
            if (cat.getRandom().nextBoolean()) {
                count++;
            }
        }
        for (int i = 0; i < count; i++) {
            cat.spawnAtLocation(new ItemStack(ModItems.PAPER_ROLL.get()));
        }
    }
}
