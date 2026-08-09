package functionhook.oldwu.cat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import functionhook.oldwu.accessor.MobAiAccessor;
import functionhook.oldwu.audio.CatAudio;
import functionhook.oldwu.entity.ModEntityTypes;
import functionhook.oldwu.entity.PaperRoll;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Server-side behaviour for cats named maodie or 耄耋. */
public final class MaodieLogic {
    public static final double MAX_HEALTH = 325.0;
    public static final double MAODIE_SCALE = 1.5;
    public static final double RAGE_THRESHOLD = 180.0;
    public static final int RAGE_FIRE_INTERVAL = 30;
    public static final int RAGE_WINDUP_TICKS = 5;
    public static final int NORMAL_FIRE_INTERVAL = 100;
    public static final float MIN_ATTACK_DAMAGE = 10.0F;
    public static final float MAX_ATTACK_DAMAGE = 20.0F;
    public static final float MELEE_ROLL_CHANCE = 0.2F;
    public static final int POISON_DURATION = 100;
    public static final int COMBO_DURATION = 160;
    public static final float POISON_CHANCE = 0.2F;
    public static final float COMBO_CHANCE = 0.1F;
    public static final double TARGET_RANGE = 128.0;
    public static final double TARGET_RANGE_SQR = TARGET_RANGE * TARGET_RANGE;
    public static final double ATTACK_RANGE_SQR = 9.0;
    public static final double MOVE_SPEED = 1.0;
    public static final int WANDER_INTERVAL_TICKS = 40;
    public static final double WANDER_RADIUS = 8.0;
    public static final int MELEE_ATTACK_MIN_DELAY = 20;
    public static final int MELEE_ATTACK_MAX_DELAY = 40;
    public static final int ATTACK_HAQI_TICKS = 12;
    public static final float ROLL_SPEED = 4.0F;
    public static final float ROLL_FAN_SPREAD = 0.2F;
    public static final int RING_COLOR = 0xCCA675;
    public static final int RING_PARTICLES_PER_TICK = 6;
    public static final double RING_RADIUS = 1.0;
    public static final double RING_DISTANCE = 1.2;
    public static final double RING_HEIGHT = 1.0;
    public static final int RAGE_RING_COLOR = 0xFF0000;
    public static final int RAGE_RING_PARTICLES_PER_TICK = 8;
    public static final double RAGE_RING_RADIUS = 1.6;
    public static final double RAGE_RING_DISTANCE = 1.4;
    public static final double RAGE_RING_HEIGHT = 1.1;
    public static final BossBarColor BOSS_BAR_COLOR = BossBarColor.YELLOW;

    private static final Map<UUID, ServerBossEvent> BOSS_EVENTS = new HashMap<>();

    private MaodieLogic() {
    }

    public static void tick(ServerLevel level, Cat cat) {
        if (!cat.isAlive()) {
            return;
        }

        initIfNeeded(cat);
        suspendVanillaAi(cat);
        ensureBossBar(level, cat);

        int attackCooldown = CatPartners.getAttackCooldown(cat);
        if (attackCooldown > 0) {
            CatPartners.setAttackCooldown(cat, attackCooldown - 1);
        }

        int haqi = CatPartners.getMaodieHaqiTimer(cat);
        if (haqi > 0) {
            CatPartners.setMaodieHaqiTimer(cat, haqi - 1);
        }

        spawnRing(level, cat, RING_COLOR, RING_PARTICLES_PER_TICK, RING_RADIUS, RING_DISTANCE, RING_HEIGHT);
        if (cat.getHealth() <= RAGE_THRESHOLD) {
            spawnRing(level, cat, RAGE_RING_COLOR, RAGE_RING_PARTICLES_PER_TICK,
                    RAGE_RING_RADIUS, RAGE_RING_DISTANCE, RAGE_RING_HEIGHT);
        }

        LivingEntity target = findTarget(cat);
        if (target == null) {
            wanderTick(cat);
            return;
        }

        boolean attacked = false;
        if (cat.distanceToSqr(target) > ATTACK_RANGE_SQR) {
            cat.getNavigation().moveTo(target, MOVE_SPEED);
            cat.getLookControl().setLookAt(target, 30.0F, 30.0F);
        } else {
            cat.getNavigation().stop();
            cat.getLookControl().setLookAt(target, 30.0F, 30.0F);
            attacked = meleeAttack(level, cat, target);
        }

        if (cat.getHealth() <= RAGE_THRESHOLD) {
            CatPartners.setMaodieNormalFireCooldown(cat, 0);
            if (rageTick(level, cat, target)) {
                extendHaqi(cat, RAGE_WINDUP_TICKS + 1);
            }
        } else {
            CatPartners.setMaodieRageCooldown(cat, 0);
            if (normalFireTick(level, cat, target)) {
                extendHaqi(cat, ATTACK_HAQI_TICKS);
            }
        }

        if (attacked) {
            extendHaqi(cat, ATTACK_HAQI_TICKS);
        }
    }

    public static void onDeath(Cat cat) {
        removeBossBar(cat);
    }

    private static void initIfNeeded(Cat cat) {
        var maxHealth = cat.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null && maxHealth.getBaseValue() != MAX_HEALTH) {
            maxHealth.setBaseValue(MAX_HEALTH);
            cat.setHealth(cat.getMaxHealth());
        }

        var scale = cat.getAttribute(Attributes.SCALE);
        if (scale != null && scale.getBaseValue() != MAODIE_SCALE) {
            scale.setBaseValue(MAODIE_SCALE);
            cat.refreshDimensions();
        }
    }

    private static void suspendVanillaAi(Cat cat) {
        MobAiAccessor accessor = (MobAiAccessor) (Object) cat;
        for (Goal.Flag flag : Goal.Flag.values()) {
            accessor.oldwu_getGoalSelector().disableControlFlag(flag);
            accessor.oldwu_getTargetSelector().disableControlFlag(flag);
        }
    }

    /**
     * 目标选择：优先最近且**视线可见**的玩家；无可见玩家时选择最近且**视线可见**的生物。
     * 耄耋只有“看到”可攻击实体才会发起攻击，不再无条件索敌。
     */
    private static LivingEntity findTarget(Cat cat) {
        Player player = findNearestPlayer(cat);
        return player != null ? player : findNearestLiving(cat);
    }

    private static Player findNearestPlayer(Cat cat) {
        List<Player> players = cat.level().getEntitiesOfClass(Player.class,
                new AABB(cat.blockPosition()).inflate(TARGET_RANGE), player -> player.isAlive()
                        && !player.isCreative() && !player.isSpectator()
                        // 仅索敌视线可见的实体
                        && cat.hasLineOfSight(player)
                        && cat.distanceToSqr(player) <= TARGET_RANGE_SQR);
        return players.stream().min(Comparator.comparingDouble(cat::distanceToSqr)).orElse(null);
    }

    private static LivingEntity findNearestLiving(Cat cat) {
        List<LivingEntity> entities = cat.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(cat.blockPosition()).inflate(TARGET_RANGE), entity -> entity != cat
                        && entity.isAlive() && !(entity instanceof ArmorStand) && !(entity instanceof Player)
                        && !(entity instanceof Cat other && CatMatingLogic.isMaodie(other))
                        // 仅索敌视线可见的实体
                        && cat.hasLineOfSight(entity)
                        && cat.distanceToSqr(entity) <= TARGET_RANGE_SQR);
        return entities.stream().min(Comparator.comparingDouble(cat::distanceToSqr)).orElse(null);
    }

    private static void extendHaqi(Cat cat, int ticks) {
        if (CatPartners.getMaodieHaqiTimer(cat) < ticks) {
            CatPartners.setMaodieHaqiTimer(cat, ticks);
        }
    }

    private static void ensureBossBar(ServerLevel level, Cat cat) {
        ServerBossEvent event = BOSS_EVENTS.computeIfAbsent(cat.getUUID(), ignored ->
                new ServerBossEvent(Component.translatable("entity.old_wu_java.maodie"), BOSS_BAR_COLOR,
                        BossBarOverlay.PROGRESS));
        event.setProgress(Math.max(0.0F, Math.min(1.0F, cat.getHealth() / cat.getMaxHealth())));
        for (ServerPlayer player : level.players()) {
            event.addPlayer(player);
        }
    }

    public static void removeBossBar(Cat cat) {
        ServerBossEvent event = BOSS_EVENTS.remove(cat.getUUID());
        if (event != null) {
            event.removeAllPlayers();
        }
    }

    private static void wanderTick(Cat cat) {
        if (cat.tickCount % WANDER_INTERVAL_TICKS != 0) {
            return;
        }
        double angle = cat.getRandom().nextDouble() * 2.0 * Math.PI;
        double radius = WANDER_RADIUS * (0.25 + cat.getRandom().nextDouble() * 0.75);
        cat.getNavigation().moveTo(cat.getX() + Math.cos(angle) * radius, cat.getY(),
                cat.getZ() + Math.sin(angle) * radius, MOVE_SPEED);
    }

    private static void spawnRing(ServerLevel level, Cat cat, int color, int perTick,
                                  double radius, double distance, double height) {
        Vec3 look = cat.getLookAngle();
        double horizontal = Math.sqrt(look.x * look.x + look.z * look.z);
        Vec3 behind = horizontal < 1.0E-4
                ? new Vec3(0.0, 0.0, 1.0)
                : new Vec3(-look.x / horizontal, 0.0, -look.z / horizontal);
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        Vec3 right = behind.cross(up);
        Vec3 center = cat.position().add(behind.scale(distance)).add(0.0, height, 0.0);
        for (int i = 0; i < perTick; i++) {
            double theta = cat.getRandom().nextDouble() * 2.0 * Math.PI;
            double r = radius * (0.85 + cat.getRandom().nextDouble() * 0.3);
            Vec3 pos = center.add(right.scale(Math.cos(theta) * r)).add(up.scale(Math.sin(theta) * r));
            level.sendParticles(new DustParticleOptions(Vec3.fromRGB24(color).toVector3f(), 1.0F), pos.x, pos.y, pos.z,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private static boolean meleeAttack(ServerLevel level, Cat cat, LivingEntity target) {
        if (CatPartners.getAttackCooldown(cat) > 0) {
            return false;
        }

        if (cat.getHealth() > RAGE_THRESHOLD && cat.getRandom().nextFloat() < MELEE_ROLL_CHANCE) {
            fireSinglePaperRoll(level, cat, target);
            CatPartners.setAttackCooldown(cat, nextMeleeDelay(cat));
            CatAudio.playHaSound(cat);
            CatPartners.setMaodieAnimTick(cat, cat.tickCount);
            return true;
        }

        float damage = MIN_ATTACK_DAMAGE + cat.getRandom().nextFloat() * (MAX_ATTACK_DAMAGE - MIN_ATTACK_DAMAGE);
        target.hurt(cat.damageSources().mobAttack(cat), damage);
        float roll = cat.getRandom().nextFloat();
        if (roll < COMBO_CHANCE) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, COMBO_DURATION, 1), cat);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, COMBO_DURATION, 1), cat);
        } else if (roll < POISON_CHANCE + COMBO_CHANCE) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 1), cat);
        }
        level.sendParticles(ParticleTypes.EXPLOSION, cat.getX(), cat.getY() + cat.getBbHeight() * 0.5,
                cat.getZ(), 2, 0.4, 0.4, 0.4, 0.1);
        CatAudio.playHaSound(cat);
        CatPartners.setMaodieAnimTick(cat, cat.tickCount);
        CatPartners.setAttackCooldown(cat, nextMeleeDelay(cat));
        return true;
    }

    private static boolean rageTick(ServerLevel level, Cat cat, LivingEntity target) {
        int cooldown = CatPartners.getMaodieRageCooldown(cat);
        if (cooldown <= 0) {
            firePaperRoll(level, cat, target);
            CatPartners.setMaodieRageCooldown(cat, RAGE_FIRE_INTERVAL);
            return true;
        }
        CatPartners.setMaodieRageCooldown(cat, cooldown - 1);
        return cooldown <= RAGE_WINDUP_TICKS;
    }

    private static void firePaperRoll(ServerLevel level, Cat cat, LivingEntity target) {
        Vec3 spawn = cat.getEyePosition();
        Vec3 aim = target.getEyePosition().subtract(spawn).normalize();
        for (int i = 0; i < 3; i++) {
            Vec3 direction = rotateAroundY(aim, (i - 1) * ROLL_FAN_SPREAD);
            PaperRoll roll = new PaperRoll(ModEntityTypes.PAPER_ROLL.get(), level);
            roll.setPos(spawn.x, spawn.y, spawn.z);
            roll.setOwner(cat);
            roll.shoot(direction.x, direction.y, direction.z, ROLL_SPEED, 0.0F);
            level.addFreshEntity(roll);
        }
        CatAudio.playHaSound(cat);
        CatPartners.setMaodieAnimTick(cat, cat.tickCount);
    }

    private static void fireSinglePaperRoll(ServerLevel level, Cat cat, LivingEntity target) {
        Vec3 spawn = cat.getEyePosition();
        Vec3 aim = target.getEyePosition().subtract(spawn).normalize();
        PaperRoll roll = new PaperRoll(ModEntityTypes.PAPER_ROLL.get(), level);
        roll.setPos(spawn.x, spawn.y, spawn.z);
        roll.setOwner(cat);
        roll.shoot(aim.x, aim.y, aim.z, ROLL_SPEED, 0.0F);
        level.addFreshEntity(roll);
    }

    private static boolean normalFireTick(ServerLevel level, Cat cat, LivingEntity target) {
        int cooldown = CatPartners.getMaodieNormalFireCooldown(cat);
        if (cooldown <= 0) {
            fireSinglePaperRoll(level, cat, target);
            CatPartners.setMaodieNormalFireCooldown(cat, NORMAL_FIRE_INTERVAL);
            CatAudio.playHaSound(cat);
            CatPartners.setMaodieAnimTick(cat, cat.tickCount);
            return true;
        }
        CatPartners.setMaodieNormalFireCooldown(cat, cooldown - 1);
        return false;
    }

    private static Vec3 rotateAroundY(Vec3 vector, float radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Vec3(vector.x * cos - vector.z * sin, vector.y,
                vector.x * sin + vector.z * cos);
    }

    private static int nextMeleeDelay(Cat cat) {
        return MELEE_ATTACK_MIN_DELAY + cat.getRandom().nextInt(MELEE_ATTACK_MAX_DELAY - MELEE_ATTACK_MIN_DELAY + 1);
    }
}
