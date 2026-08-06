package functionhook.oldwu.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.cat.CatState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 驯服猫的项圈层：原版项圈模型只与原始猫模型对齐，模组在非 COMMON 状态会替换模型，
 * 导致项圈位置错乱。这里在非 COMMON 状态或耄耋时隐藏项圈，避免错位（移植上游 1.5.0）。
 * NeoForge 1.21.1 的层渲染直接接收 Cat 实体，状态从同步实体数据读取。
 */
@Mixin(CatCollarLayer.class)
public abstract class CatCollarLayerMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void oldwu_hideCollarInCustomStates(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, Cat cat,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (CatMatingLogic.isMaodie(cat) || CatPartners.getState(cat) != CatState.COMMON) {
            ci.cancel();
        }
    }
}
