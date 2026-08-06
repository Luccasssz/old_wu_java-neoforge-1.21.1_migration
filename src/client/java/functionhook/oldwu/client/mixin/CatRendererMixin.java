package functionhook.oldwu.client.mixin;

import functionhook.oldwu.Old_Wu_java;
import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.cat.CatPartners;
import functionhook.oldwu.client.model.AngryCatBabyModel;
import functionhook.oldwu.client.model.AngryCatModel;
import functionhook.oldwu.client.model.BattleCatBabyModel;
import functionhook.oldwu.client.model.BattleCatModel;
import functionhook.oldwu.client.model.FlatCatBabyModel;
import functionhook.oldwu.client.model.FlatCatModel;
import functionhook.oldwu.client.model.MaodieCatModel;
import functionhook.oldwu.client.model.RecoveryCatBabyModel;
import functionhook.oldwu.client.model.RecoveryCatModel;
import functionhook.oldwu.client.model.GroomingCatBabyModel;
import functionhook.oldwu.client.model.GroomingCatModel;
import functionhook.oldwu.client.render.CatStateModelHolder;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatRenderer.class)
public abstract class CatRendererMixin implements CatStateModelHolder {
    @Unique
    private MaodieCatModel oldwuMaodieModel;
    @Unique
    private AngryCatModel oldwuAngryModel;
    @Unique
    private AngryCatBabyModel oldwuAngryBabyModel;
    @Unique
    private BattleCatModel oldwuBattleModel;
    @Unique
    private BattleCatBabyModel oldwuBattleBabyModel;
    @Unique
    private RecoveryCatModel oldwuRecoveryModel;
    @Unique
    private RecoveryCatBabyModel oldwuRecoveryBabyModel;
    @Unique
    private FlatCatModel oldwuFlatModel;
    @Unique
    private FlatCatBabyModel oldwuFlatBabyModel;
    @Unique
    private GroomingCatModel oldwuGroomingModel;
    @Unique
    private GroomingCatBabyModel oldwuGroomingBabyModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void oldwu_bakeModels(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.oldwuMaodieModel = new MaodieCatModel(context.bakeLayer(MaodieCatModel.LAYER_LOCATION));
        this.oldwuAngryModel = new AngryCatModel(context.bakeLayer(AngryCatModel.LAYER_LOCATION));
        this.oldwuAngryBabyModel = new AngryCatBabyModel(context.bakeLayer(AngryCatBabyModel.LAYER_LOCATION));
        this.oldwuBattleModel = new BattleCatModel(context.bakeLayer(BattleCatModel.LAYER_LOCATION));
        this.oldwuBattleBabyModel = new BattleCatBabyModel(context.bakeLayer(BattleCatBabyModel.LAYER_LOCATION));
        this.oldwuRecoveryModel = new RecoveryCatModel(context.bakeLayer(RecoveryCatModel.LAYER_LOCATION));
        this.oldwuRecoveryBabyModel = new RecoveryCatBabyModel(context.bakeLayer(RecoveryCatBabyModel.LAYER_LOCATION));
        this.oldwuFlatModel = new FlatCatModel(context.bakeLayer(FlatCatModel.LAYER_LOCATION));
        this.oldwuFlatBabyModel = new FlatCatBabyModel(context.bakeLayer(FlatCatBabyModel.LAYER_LOCATION));
        this.oldwuGroomingModel = new GroomingCatModel(context.bakeLayer(GroomingCatModel.LAYER_LOCATION));
        this.oldwuGroomingBabyModel = new GroomingCatBabyModel(context.bakeLayer(GroomingCatModel.LAYER_LOCATION));
    }

    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void oldwu_maodieTexture(Cat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (CatMatingLogic.isMaodie(entity)) {
            String texture = CatPartners.getMaodieHaqiTimer(entity) > 0
                    ? "textures/entity/haqi.png"
                    : "textures/entity/maodie.png";
            cir.setReturnValue(ResourceLocation.fromNamespaceAndPath(Old_Wu_java.MOD_ID, texture));
        }
    }

    @Override
    public MaodieCatModel oldwu_getMaodieModel() { return oldwuMaodieModel; }
    @Override
    public AngryCatModel oldwu_getAngryModel() { return oldwuAngryModel; }
    @Override
    public AngryCatBabyModel oldwu_getAngryBabyModel() { return oldwuAngryBabyModel; }
    @Override
    public BattleCatModel oldwu_getBattleModel() { return oldwuBattleModel; }
    @Override
    public BattleCatBabyModel oldwu_getBattleBabyModel() { return oldwuBattleBabyModel; }
    @Override
    public RecoveryCatModel oldwu_getRecoveryModel() { return oldwuRecoveryModel; }
    @Override
    public RecoveryCatBabyModel oldwu_getRecoveryBabyModel() { return oldwuRecoveryBabyModel; }
    @Override
    public FlatCatModel oldwu_getFlatModel() { return oldwuFlatModel; }
    @Override
    public FlatCatBabyModel oldwu_getFlatBabyModel() { return oldwuFlatBabyModel; }
    @Override
    public GroomingCatModel oldwu_getGroomingModel() { return oldwuGroomingModel; }
    @Override
    public GroomingCatBabyModel oldwu_getGroomingBabyModel() { return oldwuGroomingBabyModel; }
}
