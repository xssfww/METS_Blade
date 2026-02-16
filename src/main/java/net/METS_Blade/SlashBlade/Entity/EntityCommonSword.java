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
public class EntityCommonSword extends EntityAbstractSummonedSword {
    private static final EntityDataAccessor<Boolean> IT_FIRED;
    Vec3 TargetPos = Vec3.ZERO;
    long fireTime = -1L;
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
    }
    @Override
    public void rideTick() {
        if (this.getVehicle() instanceof LivingEntity sender) {
            if (sender.level() instanceof ServerLevel) {
                if (this.itFired() && this.fireTime <= (long) this.tickCount) {
                    this.stopRiding();
                    this.tickCount = 0;
                    this.shoot(TargetPos.x, TargetPos.y, TargetPos.z, 3.0F, 1.0F);
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.setRot(TargetPos);
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
        Vec3 vec3d = (new Vec3(dir.x, dir.y, dir.z)).normalize().add(this.random.nextGaussian() * 0.007499999832361937 * 1.0F, this.random.nextGaussian() * 0.007499999832361937 * 1.0F, this.random.nextGaussian() * 0.007499999832361937 * 1.0F).scale(3.0F);
        float f = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
        this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setXRot((float) (Mth.atan2(vec3d.y, f) * 57.2957763671875));
    }
    public void SetTargetPos(Vec3 dir) {
        this.TargetPos = dir;
    }
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
    static {
        IT_FIRED = SynchedEntityData.defineId(EntityCommonSword.class, EntityDataSerializers.BOOLEAN);
    }
}

