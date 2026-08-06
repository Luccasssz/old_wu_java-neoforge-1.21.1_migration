package functionhook.oldwu.entity;

import functionhook.oldwu.item.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Straight-flying paper tube projectile used by players and maodie. */
public class PaperRoll extends AbstractHurtingProjectile {
    public static final float DAMAGE = 15.0F;
    public static final float EXPLOSION_POWER = 1.0F;

    public PaperRoll(EntityType<? extends PaperRoll> type, Level level) {
        super(type, level);
        this.accelerationPower = 0.0;
    }

    public PaperRoll(Level level, LivingEntity shooter, Vec3 direction) {
        super(ModEntityTypes.PAPER_ROLL.get(), shooter, direction, level);
        this.accelerationPower = 0.0;
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity target = hitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource source = owner instanceof LivingEntity living
                ? this.damageSources().mobProjectile(this, living)
                : this.damageSources().generic();
        target.hurt(source, DAMAGE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER,
                    false, ExplosionInteraction.MOB);
            this.discard();
        }
    }
}
