package functionhook.oldwu.entity;

import functionhook.oldwu.cat.CatMatingLogic;
import functionhook.oldwu.item.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Straight-flying paper tube projectile used by players and maodie.
 * <p>maodie 不会受到自己发射的纸卷的直接伤害，也不会受到其爆炸的伤害。
 */
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

    /**
     * 是否为 maodie 发射的纸卷（发射者是命名为 maodie 的猫）。
     */
    private static boolean isMaodieOwner(Entity owner) {
        return owner instanceof Cat cat && CatMatingLogic.isMaodie(cat);
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
        if (isMaodieOwner(owner) && target == owner) {
            return;
        }
        DamageSource source = owner instanceof LivingEntity living
                ? this.damageSources().mobProjectile(this, living)
                : this.damageSources().generic();
        target.hurt(source, DAMAGE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            Entity owner = this.getOwner();
            this.level().explode(
                    this,
                    Explosion.getDefaultDamageSource(this.level(), this),
                    new ExplosionDamageCalculator() {
                        @Override
                        public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
                            if (isMaodieOwner(owner) && entity == owner) {
                                return false;
                            }
                            return super.shouldDamageEntity(explosion, entity);
                        }
                    },
                    this.getX(), this.getY(), this.getZ(),
                    EXPLOSION_POWER,
                    false,
                    ExplosionInteraction.MOB
            );
            this.discard();
        }
    }
}
