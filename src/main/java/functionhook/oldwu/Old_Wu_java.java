package functionhook.oldwu;

import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import functionhook.oldwu.cat.GoodCatLogic;
import functionhook.oldwu.block.ModBlocks;
import functionhook.oldwu.effect.ModEffects;
import functionhook.oldwu.entity.ModEntityTypes;
import functionhook.oldwu.item.GounaiDrinkTracker;
import functionhook.oldwu.item.ModItems;
import functionhook.oldwu.particle.ModParticles;
import functionhook.oldwu.sound.ModSounds;
import functionhook.oldwu.attribute.ModAttributes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** NeoForge entry point for the Old Wu Java cat behaviour mod. */
@Mod(Old_Wu_java.MOD_ID)
public final class Old_Wu_java {
    public static final String MOD_ID = "old_wu_java";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Old_Wu_java(IEventBus modEventBus) {
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModItems.CREATIVE_MODE_TABS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        GounaiDrinkTracker.ATTACHMENT_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(Old_Wu_java::onEntityInteract);
        NeoForge.EVENT_BUS.addListener(Old_Wu_java::onEntityInteractSpecific);
        LOGGER.info("Loaded Old Wu Java NeoForge port");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /** Shovel right-click immediately flattens a cat, matching the upstream callback. */
    private static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (isShovelInteract(event, event.getTarget())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            flattenFromInteraction(event, event.getTarget());
        }
    }

    private static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (isShovelInteract(event, event.getTarget())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            flattenFromInteraction(event, event.getTarget());
        }
    }

    private static boolean isShovelInteract(PlayerInteractEvent event, Entity target) {
        if (!(target instanceof Cat cat)
                || !(event.getItemStack().getItem() instanceof ShovelItem)) {
            return false;
        }
        return true;
    }

    private static void flattenFromInteraction(PlayerInteractEvent event, Entity target) {
        Cat cat = (Cat) target;
        if (!event.getLevel().isClientSide()) {
            CatMatingLogic.enterFlat(cat);
            // 铲子压扁：坏猫/键帽 50% 概率好猫值 +1
            if (GoodCatLogic.getGoodValue(cat) < GoodCatLogic.BAD_THRESHOLD
                    && cat.getRandom().nextBoolean()) {
                GoodCatLogic.setGoodValue(cat, GoodCatLogic.getGoodValue(cat) + 1);
            }
            event.getLevel().playSound(null, cat, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
