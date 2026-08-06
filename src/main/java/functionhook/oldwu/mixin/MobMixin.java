package functionhook.oldwu.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.ai.goal.GoalSelector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import functionhook.oldwu.accessor.MobAiAccessor;

@Mixin(Mob.class)
public abstract class MobMixin implements MobAiAccessor {
	/**
	 * 清除旧版本可能残留在猫身上的 NoAI 标记。
	 *
	 * <p>耄耋现在需要保留 server AI tick 以执行自己的导航和攻击逻辑；
	 * {@code MaodieLogic} 会可逆地禁用原版目标/行为控制标志，而不是永久删除 Goal。
	 */
	@Inject(method = "isEffectiveAi", at = @At("HEAD"), cancellable = true)
	private void oldwu_maodieNoAi(CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof Cat cat && cat.isNoAi()) {
			// 清除历史版本 setNoAi(true) 残留的 NoAI 标志，改名后恢复原版 AI
			cat.setNoAi(false);
		}
	}

	@Accessor("goalSelector")
	@Override
	public abstract GoalSelector oldwu_getGoalSelector();

	@Accessor("targetSelector")
	@Override
	public abstract GoalSelector oldwu_getTargetSelector();
}
