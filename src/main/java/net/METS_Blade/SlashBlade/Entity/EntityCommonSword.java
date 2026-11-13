package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
public class EntityCommonSword extends EntityAbstractSummonedSword {
    private static final EntityDataAccessor<Boolean> IS_FIRED = SynchedEntityData.defineId(EntityCommonSword.class, EntityDataSerializers.BOOLEAN);
    private static final double SPEED = 3.0;
    private static final double TURN_RATE = 0.3;
    private static final double AIM_RANDOM_FACTOR = 0.007499999832361937;
    private static final double AIM_SCALE = 3.0;
    private Vec3 targetPos = Vec3.ZERO;
    private int targetId = -1;
    private long fireTime = -1L;
    public EntityCommonSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    @Override
    public void tick() {
        if (!isFired() && getVehicle() == null && getOwner() != null) {
            startRiding(getOwner(), true);
        }
        super.tick();
        if (isFired() && !level().isClientSide && tickCount > 0) {
            LivingEntity target = getTrackingTarget();
            if (target != null) {
                steerTowardTarget(target);
            }
        }
    }
    @Override
    public void rideTick() {
        if (!(getVehicle() instanceof LivingEntity sender)) return;
        if (!(sender.level() instanceof ServerLevel)) return;
        if (isFired() && fireTime <= (long) tickCount) {
            stopRiding();
            tickCount = 0;
            Vec3 initDir = targetPos.normalize();
            setDeltaMovement(initDir.scale(SPEED));
            setRot(initDir);
        } else {
            setDeltaMovement(Vec3.ZERO);
            setRot(targetPos);
            baseTick();
            if (!isFired()) {
                fireTime = tickCount + getDelay();
                doFire();
            }
        }
    }
    public void setRot(Vec3 dir) {
        Vec3 aimed = addRandomAimOffset(dir);
        float horizontalDist = Mth.sqrt((float) aimed.horizontalDistanceSqr());
        setYRot((float) (Mth.atan2(aimed.x, aimed.z) * 180.0 / Math.PI));
        setXRot((float) (Mth.atan2(aimed.y, horizontalDist) * 180.0 / Math.PI));
    }
    private Vec3 addRandomAimOffset(Vec3 baseDir) {
        return baseDir.normalize()
                .add(random.nextGaussian() * AIM_RANDOM_FACTOR, random.nextGaussian() * AIM_RANDOM_FACTOR, random.nextGaussian() * AIM_RANDOM_FACTOR)
                .scale(AIM_SCALE);
    }
    public void setTargetPos(Vec3 dir) {
        this.targetPos = dir;
    }
    public void setTargetId(int id) {
        this.targetId = id;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_FIRED, false);
    }
    public void doFire() {
        this.getEntityData().set(IS_FIRED, true);
    }
    public boolean isFired() {
        return this.getEntityData().get(IS_FIRED);
    }
    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        super.burst();
    }
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        if (targetEntity instanceof LivingEntity entity) {
            KnockBacks.cancel.action.accept(entity);
            StunManager.setStun(entity);
        }
        super.onHitEntity(entityHitResult);
    }
    @Nullable
    private LivingEntity getTrackingTarget() {
        if (targetId != -1) {
            Entity e = level().getEntity(targetId);
            if (e instanceof LivingEntity living && living.isAlive()) {
                return living;
            }
        }
        return null;
    }
    private void steerTowardTarget(LivingEntity target) {
        Vec3 Pos = this.position();
        Vec3 targetPos = target.position();
        Vec3 desiredDir = targetPos.subtract(Pos).normalize();
        Vec3 currentDir = this.getDeltaMovement().normalize();
        Vec3 newDir = currentDir.add(desiredDir.subtract(currentDir).scale(TURN_RATE)).normalize();
        setDeltaMovement(newDir.scale(SPEED));
        setRot(newDir);
    }
}