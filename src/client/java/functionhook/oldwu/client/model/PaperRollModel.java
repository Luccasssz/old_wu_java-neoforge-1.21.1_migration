package functionhook.oldwu.client.model;

import functionhook.oldwu.Old_Wu_java;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import functionhook.oldwu.entity.PaperRoll;

/**
 * 纸筒实体模型。几何与源模组一致：模型居中，长轴沿 Z 轴，供渲染器按 yaw/pitch
 * 直接对齐飞行方向。
 */
public class PaperRollModel extends EntityModel<PaperRoll> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Old_Wu_java.id("paper_roll"), "main");
    private final ModelPart root;

    public PaperRollModel(ModelPart root) {
        super();
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition rootDefinition = meshDefinition.getRoot();

        PartDefinition bone = rootDefinition.addOrReplaceChild(
                "bone",
                CubeListBuilder.create()
                        .texOffs(21, 0).addBox(-5.0F, -23.0F, 0.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-6.0F, -23.0F, -4.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(7, 0).addBox(-6.0F, -23.0F, -2.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(14, 0).addBox(-5.0F, -23.0F, -6.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 0).addBox(4.0F, -23.0F, -6.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 26).addBox(5.0F, -23.0F, -4.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(35, 0).addBox(5.0F, -23.0F, -2.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(35, 26).addBox(4.0F, -23.0F, 0.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -2.0F, 11.5F, 1.5708F, 0.0F, 0.0F));

        addSide(bone, "cube_r1", 49, 26, -4.0F, 3.0F);
        addSide(bone, "cube_r2", 49, 0, -2.0F, 4.0F);
        addSide(bone, "cube_r3", 42, 26, 0.0F, 4.0F);
        addSide(bone, "cube_r4", 42, 0, 2.0F, 3.0F);
        addSide(bone, "cube_r5", 21, 26, 2.0F, -6.0F);
        addSide(bone, "cube_r6", 14, 26, 0.0F, -7.0F);
        addSide(bone, "cube_r7", 7, 26, -2.0F, -7.0F);
        addSide(bone, "cube_r8", 0, 26, -4.0F, -6.0F);

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private static void addSide(PartDefinition bone, String name, int textureX, int textureY,
                                float offsetX, float offsetZ) {
        bone.addOrReplaceChild(
                name,
                CubeListBuilder.create().texOffs(textureX, textureY)
                        .addBox(0.0F, -23.0F, 0.0F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(offsetX, 0.0F, offsetZ, 0.0F, 1.5708F, 0.0F));
    }

    @Override
    public void setupAnim(PaperRoll entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
