package functionhook.oldwu.particle;

import functionhook.oldwu.Old_Wu_java;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Old_Wu_java.MOD_ID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RECOVERY =
            PARTICLE_TYPES.register("recovery", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAOMAO =
            PARTICLE_TYPES.register("maomao", () -> new SimpleParticleType(false));

    private ModParticles() {}
}
