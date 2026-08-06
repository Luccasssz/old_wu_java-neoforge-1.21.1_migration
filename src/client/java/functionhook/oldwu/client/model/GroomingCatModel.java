package functionhook.oldwu.client.model;

import functionhook.oldwu.Old_Wu_java;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import functionhook.oldwu.client.animation.CatAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.Cat;

/** Cat model used while a water splash is grooming the cat. */
public class GroomingCatModel extends OldWuCatModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Old_Wu_java.id("cat_grooming"), "main");

    public GroomingCatModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 1.0F));
        body.addOrReplaceChild("belly", CubeListBuilder.create()
                .texOffs(20, 0).addBox(-2.0F, -8.0F, -3.0F, 4.0F, 6.0F, 6.0F)
                .texOffs(20, 11).addBox(-2.0F, 3.0F, -3.0F, 4.0F, 5.0F, 6.0F)
                .texOffs(20, 6).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 5.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F)
                .texOffs(0, 24).addBox(-1.5F, -0.0156F, -4.0F, 3.0F, 2.0F, 2.0F)
                .texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, -2.0F, -10.0F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create()
                .texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 7.0F, 0.7854F, 0.0F, 0.0F));
        tail1.addOrReplaceChild("tail2", CubeListBuilder.create()
                .texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        body.addOrReplaceChild("backLegL", leg(8, 13), PartPose.offset(1.1F, 1.0F, 6.0F));
        body.addOrReplaceChild("backLegR", leg(8, 13), PartPose.offset(-1.1F, 1.0F, 6.0F));
        body.addOrReplaceChild("frontLegL", leg(40, 0, 10), PartPose.offset(1.2F, -3.0F, -5.0F));
        body.addOrReplaceChild("frontLegR", leg(40, 0, 10), PartPose.offset(-1.2F, -3.0F, -5.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static CubeListBuilder leg(int u, int v) {
        return leg(u, v, 6);
    }

    private static CubeListBuilder leg(int u, int v, int height) {
        return CubeListBuilder.create().texOffs(u, v).addBox(-1.0F, -0.2F, -1.0F, 2.0F, height, 2.0F,
                new CubeDeformation(0.0F));
    }

    @Override
    public void setupAnim(Cat cat, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        oldwuResetPose();
        if (CatPartners.getState(cat) == CatState.GROOMING) {
            oldwuAnimate(CatAnimations.GROOMING, ageInTicks);
        }
    }
}
