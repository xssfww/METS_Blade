package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.SlashBlade;
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
    protected static final EntityDataAccessor<Boolean> IT_FIRED;
    private Vec3 targetPos = Vec3.ZERO;
    private int targetId = -1;
    private long fireTime = -1L;
    private static final double SPEED = 3.0;
    private static final double TURN_RATE = 0.3;
    public EntityCommonSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    public static EntityCommonSword createInstance(Level worldIn) {
        return new EntityCommonSword(SlashBlade.RegistryEvents.SummonedSword, worldIn);
    }
    @Override
    public void tick() {
        if (!this.itFired() && this.getVehicle() == null && this.getOwner() != null) {
            this.startRiding(this.getOwner(), true);
        }
        super.tick();
        if (this.itFired() && !this.level().isClientSide && this.tickCount > 0) {
            LivingEntity target = getTrackingTarget();
            if (target != null) {
                steerTowardTarget(target);
            }
        }
    }
    @Override
    public void rideTick() {
        if (this.getVehicle() instanceof LivingEntity sender) {
            if (sender.level() instanceof ServerLevel) {
                if (this.itFired() && this.fireTime <= (long) this.tickCount) {
                    this.stopRiding();
                    this.tickCount = 0;
                    Vec3 initDir = targetPos.normalize();
                    this.setDeltaMovement(initDir.scale(SPEED));
                    this.setRot(initDir);
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.setRot(targetPos);
                    if (this.canUpdate()) {
                        this.baseTick();
                    }
                    if (!this.itFired()) {
                        this.fireTime = this.tickCount + this.getDelay();
                        this.doFire();
                    }
                }
            }
        }
    }
    public void setRot(Vec3 dir) {
        Vec3 vec3d = dir.normalize()
                .add(
                        this.random.nextGaussian() * 0.007499999832361937,
                        this.random.nextGaussian() * 0.007499999832361937,
                        this.random.nextGaussian() * 0.007499999832361937
                )
                .scale(3.0F);
        float horizontalDist = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
        this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 180.0 / Math.PI));
        this.setXRot((float) (Mth.atan2(vec3d.y, horizontalDist) * 180.0 / Math.PI));
    }
    public void setTargetPos(Vec3 dir) {
        this.targetPos = dir;
    }
    public void setTargetId(int id) {
        this.targetId = id;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IT_FIRED, false);
    }
    public void doFire() {
        this.getEntityData().set(IT_FIRED, true);
    }
    public boolean itFired() {
        return this.getEntityData().get(IT_FIRED);
    }
    @Override
    protected void onHitBlock(BlockHitResult blockHitresult) {
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
        if (this.targetId != -1) {
            Entity e = this.level().getEntity(this.targetId);
            if (e instanceof LivingEntity living && living.isAlive()) {
                return living;
            }
        }
        return null;
    }
    private void steerTowardTarget(LivingEntity target) {
        Vec3 myPos = this.position();
        Vec3 targetPos = target.position();
        Vec3 desiredDir = targetPos.subtract(myPos).normalize();
        Vec3 currentDir = this.getDeltaMovement().normalize();
        Vec3 newDir = currentDir.add(desiredDir.subtract(currentDir).scale(TURN_RATE)).normalize();
        this.setDeltaMovement(newDir.scale(SPEED));
        this.setRot(newDir);
    }
    static {
        IT_FIRED = SynchedEntityData.defineId(EntityCommonSword.class, EntityDataSerializers.BOOLEAN);
    }
}

