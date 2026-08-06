package functionhook.oldwu.client;

import functionhook.oldwu.Old_Wu_java;
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
import functionhook.oldwu.client.model.PaperRollModel;
import functionhook.oldwu.client.particle.MaomaoParticle;
import functionhook.oldwu.client.particle.RecoveryParticle;
import functionhook.oldwu.client.render.PaperRollRenderer;
import functionhook.oldwu.entity.ModEntityTypes;
import functionhook.oldwu.particle.ModParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = Old_Wu_java.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class Old_Wu_javaClient {
    private Old_Wu_javaClient() {}

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MaodieCatModel.LAYER_LOCATION, MaodieCatModel::createBodyLayer);
        event.registerLayerDefinition(AngryCatModel.LAYER_LOCATION, AngryCatModel::createBodyLayer);
        event.registerLayerDefinition(AngryCatBabyModel.LAYER_LOCATION, AngryCatBabyModel::createBodyLayer);
        event.registerLayerDefinition(BattleCatModel.LAYER_LOCATION, BattleCatModel::createBodyLayer);
        event.registerLayerDefinition(BattleCatBabyModel.LAYER_LOCATION, BattleCatBabyModel::createBodyLayer);
        event.registerLayerDefinition(RecoveryCatModel.LAYER_LOCATION, RecoveryCatModel::createBodyLayer);
        event.registerLayerDefinition(RecoveryCatBabyModel.LAYER_LOCATION, RecoveryCatBabyModel::createBodyLayer);
        event.registerLayerDefinition(FlatCatModel.LAYER_LOCATION, FlatCatModel::createBodyLayer);
        event.registerLayerDefinition(FlatCatBabyModel.LAYER_LOCATION, FlatCatBabyModel::createBodyLayer);
        event.registerLayerDefinition(GroomingCatModel.LAYER_LOCATION, GroomingCatModel::createBodyLayer);
        event.registerLayerDefinition(PaperRollModel.LAYER_LOCATION, PaperRollModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.PAPER_ROLL.get(), PaperRollRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RECOVERY.get(), RecoveryParticle.Provider::new);
        event.registerSpriteSet(ModParticles.MAOMAO.get(), MaomaoParticle.Provider::new);
    }
}
