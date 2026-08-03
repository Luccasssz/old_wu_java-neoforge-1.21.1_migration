package functionhook.oldwu.client.mixin;

import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import functionhook.oldwu.client.model.OldWuCatModel;
import functionhook.oldwu.client.render.CatStateModelHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow
    protected M model;

    @Unique
    private M oldwu_previousModel;

    @Inject(method = "render", at = @At("HEAD"))
    @SuppressWarnings("unchecked")
    private void oldwu_swapStateModel(
            T entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!(entity instanceof Cat cat)
                || !((Object) this instanceof CatRenderer)
                || !((Object) this instanceof CatStateModelHolder holder)) {
            return;
        }

        EntityModel<?> selected = null;
        boolean baby = cat.isBaby();
        if (CatMatingLogic.isMaodie(cat)) {
            selected = holder.oldwu_getMaodieModel();
        } else {
            CatState state = CatPartners.getState(cat);
            if (state == CatState.DANCE) {
                selected = switch (CatPartners.getDanceModelIndex(cat)) {
                    case 1 -> baby ? holder.oldwu_getAngryBabyModel() : holder.oldwu_getAngryModel();
                    case 2 -> baby ? holder.oldwu_getBattleBabyModel() : holder.oldwu_getBattleModel();
                    case 3 -> baby ? holder.oldwu_getRecoveryBabyModel() : holder.oldwu_getRecoveryModel();
                    case 4 -> baby ? holder.oldwu_getFlatBabyModel() : holder.oldwu_getFlatModel();
                    default -> null;
                };
            } else if (state == CatState.ANGRY || state == CatState.PAIRING) {
                selected = baby ? holder.oldwu_getAngryBabyModel() : holder.oldwu_getAngryModel();
            } else if (state == CatState.BATTLE) {
                selected = baby ? holder.oldwu_getBattleBabyModel() : holder.oldwu_getBattleModel();
            } else if (state == CatState.RECOVERY) {
                selected = baby ? holder.oldwu_getRecoveryBabyModel() : holder.oldwu_getRecoveryModel();
            } else if (state == CatState.FLAT) {
                selected = baby ? holder.oldwu_getFlatBabyModel() : holder.oldwu_getFlatModel();
            }
        }

        if (selected != null) {
            this.oldwu_previousModel = this.model;
            this.model = (M) selected;
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void oldwu_restoreStateModel(
            T entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (this.oldwu_previousModel != null) {
            this.model = this.oldwu_previousModel;
            this.oldwu_previousModel = null;
        }
    }
}
