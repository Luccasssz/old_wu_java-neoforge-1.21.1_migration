package functionhook.oldwu.mixin;

import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Starts the water-powered grooming/peace state on nearby cats. */
@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin {
    @Inject(method = "applyWater", at = @At("HEAD"))
    private void oldwu_triggerGrooming(CallbackInfo ci) {
        ThrownPotion potion = (ThrownPotion) (Object) this;
        if (!(potion.level() instanceof ServerLevel level)) {
            return;
        }

        AABB splashArea = potion.getBoundingBox().inflate(4.0, 2.0, 4.0);
        for (Cat cat : level.getEntitiesOfClass(Cat.class, splashArea)) {
            if (potion.distanceToSqr(cat) >= 16.0) {
                continue;
            }
            CatPartners.setGroomingTimer(cat, 100);
            CatPartners.setBattlePeaceTimer(cat, 600);
            CatPartners.setState(cat, CatState.GROOMING);
            CatPartners.setPartner(cat, null);
            cat.getNavigation().stop();
        }
    }
}
