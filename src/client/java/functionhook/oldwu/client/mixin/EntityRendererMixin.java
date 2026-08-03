package functionhook.oldwu.client.mixin;

import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Makes the recovery outline green on the 1.21.1 entity-outline path. */
@Mixin(Entity.class)
public abstract class EntityRendererMixin {
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void oldwu_recoveryOutline(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof Cat cat && CatPartners.getState(cat) == CatState.RECOVERY) {
            cir.setReturnValue(0x00FF00);
        }
    }
}
