package functionhook.oldwu.accessor;

import net.minecraft.world.entity.ai.goal.GoalSelector;

/** Access to the two vanilla selectors so maodie can suspend them reversibly. */
public interface MobAiAccessor {
    GoalSelector oldwu_getGoalSelector();

    GoalSelector oldwu_getTargetSelector();
}
