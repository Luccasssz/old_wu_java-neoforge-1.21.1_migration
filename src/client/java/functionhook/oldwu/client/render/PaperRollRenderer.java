package functionhook.oldwu.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import functionhook.oldwu.Old_Wu_java;
import functionhook.oldwu.client.model.PaperRollModel;
import functionhook.oldwu.entity.PaperRoll;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** 1.21.1 renderer for the straight-flying paper-tube entity. */
public class PaperRollRenderer extends EntityRenderer<PaperRoll> {
    private static final ResourceLocation TEXTURE = Old_Wu_java.id("textures/entity/paper_roll.png");
    private final PaperRollModel model;

    public PaperRollRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PaperRollModel(context.bakeLayer(PaperRollModel.LAYER_LOCATION));
    }

    @Override
    protected int getBlockLightLevel(PaperRoll entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(PaperRoll entity) {
        return TEXTURE;
    }

    @Override
    public void render(PaperRoll entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
