package functionhook.oldwu.attribute;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** NeoForge attribute registrations for the dagoujiao (Big Dog Bark) system. */
public final class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, Old_Wu_java.MOD_ID);

    /**
     * 额外最大生命值（无上限，同步客户端），用于突破原版
     * {@code minecraft:generic.max_health} 的 1024 上限，让“大狗叫”喂食 64 次
     * 能达到设计所需的最大生命值。
     */
    public static final DeferredHolder<Attribute, Attribute> EXTRA_MAX_HEALTH =
            ATTRIBUTES.register("extra_max_health", () -> new RangedAttribute(
                    ResourceLocation.fromNamespaceAndPath(Old_Wu_java.MOD_ID, "extra_max_health").toLanguageKey(),
                    0.0, 0.0, Double.MAX_VALUE).setSyncable(true));

    /** 大狗叫蓄力进度（0~12），同步到客户端供喂食 HUD 的绿色蓄力条显示。 */
    public static final DeferredHolder<Attribute, Attribute> CHARGE =
            ATTRIBUTES.register("charge", () -> new RangedAttribute(
                    ResourceLocation.fromNamespaceAndPath(Old_Wu_java.MOD_ID, "charge").toLanguageKey(),
                    0.0, 0.0, 12.0).setSyncable(true));

    private ModAttributes() {
    }

    @EventBusSubscriber(modid = Old_Wu_java.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class ModBusEvents {
        @SubscribeEvent
        public static void onAttributeModification(EntityAttributeModificationEvent event) {
            event.add(EntityType.WOLF, EXTRA_MAX_HEALTH);
            event.add(EntityType.WOLF, CHARGE);
        }
    }
}
