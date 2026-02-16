package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
public class EntityBloodSword extends EntityCommonSword {
    public EntityBloodSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    public static EntityBloodSword createInstance(Level worldIn) {
        return new EntityBloodSword(SlashBlade.RegistryEvents.SummonedSword, worldIn);
    }
    @Override
    public void tick() {
        super.tick();
    }
    @Override
    public void rideTick() {
        super.rideTick();
    }
    @Override
    protected void onHitBlock(BlockHitResult blockHitresult) {
        if (this.getShooter() != null) {
            if (this.level() instanceof ServerLevel world) {
                world.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0,0.0,0.0);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 0.7F, 1.6F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.4F);
            }
        }
        super.burst();
    }
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        if (targetEntity instanceof LivingEntity entity) {
            KnockBacks.cancel.action.accept(entity);
            StunManager.setStun(entity);
            if (this.getShooter() != null) {
                if (this.level() instanceof ServerLevel Level) {
                    Level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0,0.0,0.0);
                    this.playSound(SoundEvents.GENERIC_EXPLODE, 0.7F, 1.6F + (Level.getRandom().nextFloat() - Level.getRandom().nextFloat()) * 0.4F);
                    entity.hurt(this.damageSources().explosion(this.getShooter(),this), (float) this.getDamage());
                    entity.invulnerableTime = 0;
                    entity.hurt(this.damageSources().indirectMagic(this.getShooter(),this), (float) this.getDamage());
                    entity.invulnerableTime = 0;
                }
            }
        }
    }
}
