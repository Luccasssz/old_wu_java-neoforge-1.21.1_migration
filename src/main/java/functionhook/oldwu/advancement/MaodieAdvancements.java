package functionhook.oldwu.advancement;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/** Helpers for the two maodie advancements. */
public final class MaodieAdvancements {
    private static final ResourceLocation DEFEAT_MAODIE_ID = Old_Wu_java.id("old_friend_gone");

    private MaodieAdvancements() {
    }

    public static void awardDefeatMaodie(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        AdvancementHolder advancement = level.getServer().getAdvancements().get(DEFEAT_MAODIE_ID);
        if (advancement != null) {
            player.getAdvancements().award(advancement, "defeat_maodie");
        }
    }
}
