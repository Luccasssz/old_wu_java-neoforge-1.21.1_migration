package functionhook.oldwu.effect;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 模组自定义状态效果注册。
 *
 * <p>{@code ETERNAL}（永恒）：饮用野生狗奶后获得，无限时长；玩家拥有该效果时
 * 不会受到任何伤害、喝牛奶/蜂蜜也无法解除（具体行为见 {@code EternalEffectMixin}
 * 与客户端 {@code HudHeartMixin}）。
 */
public final class ModEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS =
		DeferredRegister.create(Registries.MOB_EFFECT, Old_Wu_java.MOD_ID);

	public static final DeferredHolder<MobEffect, EternalEffect> ETERNAL =
		MOB_EFFECTS.register("eternal", EternalEffect::new);

	private ModEffects() {
	}
}
