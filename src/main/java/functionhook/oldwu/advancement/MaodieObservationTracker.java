package functionhook.oldwu.advancement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import functionhook.oldwu.Old_Wu_java;
import functionhook.oldwu.cat.CatMatingLogic;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Counts two separate, server-side spyglass observations of a maodie. */
@EventBusSubscriber(modid = Old_Wu_java.MOD_ID)
public final class MaodieObservationTracker {
    private static final ResourceLocation ADVANCEMENT_ID = Old_Wu_java.id("spot_check_camera");
    private static final double OBSERVATION_RANGE = 64.0;
    private static final Map<UUID, ObservationState> STATES = new HashMap<>();

    private MaodieObservationTracker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !(player.level() instanceof ServerLevel level)) {
            return;
        }

        ObservationState state = STATES.computeIfAbsent(player.getUUID(), ignored -> new ObservationState());
        boolean observing = isObservingMaodie(player);
        if (!observing) {
            state.observing = false;
            return;
        }

        if (!state.observing) {
            state.observations++;
            if (state.observations >= 2) {
                AdvancementHolder advancement = level.getServer().getAdvancements().get(ADVANCEMENT_ID);
                if (advancement != null) {
                    player.getAdvancements().award(advancement, "observe_maodie");
                }
                state.observations = 0;
            }
        }
        state.observing = true;
    }

    private static boolean isObservingMaodie(ServerPlayer player) {
        ItemStack useItem = player.getUseItem();
        if (!player.isUsingItem() || !useItem.is(Items.SPYGLASS)) {
            return false;
        }

        Vec3 start = player.getEyePosition();
        Vec3 view = player.getViewVector(1.0F).normalize();
        Cat target = null;
        double nearest = Double.MAX_VALUE;
        for (Cat cat : player.level().getEntitiesOfClass(Cat.class,
                player.getBoundingBox().inflate(OBSERVATION_RANGE))) {
            if (!CatMatingLogic.isMaodie(cat) || !player.hasLineOfSight(cat)) {
                continue;
            }
            double distance = cat.distanceToSqr(start);
            if (distance > OBSERVATION_RANGE * OBSERVATION_RANGE) {
                continue;
            }
            Vec3 toCat = cat.getBoundingBox().getCenter().subtract(start).normalize();
            if (view.dot(toCat) < 0.985) {
                continue;
            }
            if (distance < nearest) {
                nearest = distance;
                target = cat;
            }
        }
        return target != null;
    }

    private static final class ObservationState {
        private int observations;
        private boolean observing;
    }
}
