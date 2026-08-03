package functionhook.oldwu.mixin;

import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class BattleCollisionMixin {
	@Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void oldWuJava$keepPairedBattleCatsInPlace(Entity other, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof Cat cat) || !(other instanceof Cat otherCat)) {
			return;
		}

		if (CatPartners.getState(cat) == CatState.BATTLE
			&& CatPartners.getState(otherCat) == CatState.BATTLE
			&& CatPartners.getPartner(cat).filter(otherCat.getUUID()::equals).isPresent()
			&& CatPartners.getPartner(otherCat).filter(cat.getUUID()::equals).isPresent()) {
			callbackInfo.cancel();
		}
	}
}
