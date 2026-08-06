package functionhook.oldwu.entity;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** NeoForge entity registrations for Old Wu Java. */
public final class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Old_Wu_java.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<PaperRoll>> PAPER_ROLL =
            ENTITY_TYPES.register("paper_roll", () -> EntityType.Builder
                    .<PaperRoll>of(PaperRoll::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(Old_Wu_java.MOD_ID + ":paper_roll"));

    private ModEntityTypes() {
    }
}
