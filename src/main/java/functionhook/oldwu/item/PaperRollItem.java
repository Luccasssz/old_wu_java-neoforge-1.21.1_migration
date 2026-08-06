package functionhook.oldwu.item;

import functionhook.oldwu.entity.ModEntityTypes;
import functionhook.oldwu.entity.PaperRoll;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/** Chargeable paper-tube item. */
public class PaperRollItem extends Item {
    public static final int MAX_CHARGE_TICKS = 20;
    public static final float MAX_SPEED = 2.5F;
    public static final float MIN_CHARGE = 0.1F;

    public PaperRollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (!(entity instanceof Player player)) {
            return;
        }

        int timeHeld = this.getUseDuration(stack, entity) - timeCharged;
        float charge = Math.min(timeHeld / (float) MAX_CHARGE_TICKS, 1.0F);
        if (charge < MIN_CHARGE) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            PaperRoll roll = new PaperRoll(ModEntityTypes.PAPER_ROLL.get(), serverLevel);
            roll.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            roll.setOwner(player);
            roll.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
                    charge * MAX_SPEED, 0.0F);
            serverLevel.addFreshEntity(roll);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
