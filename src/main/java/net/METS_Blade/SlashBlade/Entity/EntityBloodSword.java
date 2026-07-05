package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.METS_Blade.SlashBlade.DamageSource.MixDamageSource;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
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
                this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.7F, 1.6F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.4F);
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
            if (this.getShooter() != null && this.level() instanceof ServerLevel Level) {
                Registry<DamageType> damageTypes = Level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                MixDamageSource source = new MixDamageSource(
                        damageTypes.getHolderOrThrow(DamageTypes.EXPLOSION),
                        this,
                        this.getShooter(),
                        this.position()
                );
                Level.sendParticles(
                        ParticleTypes.EXPLOSION,
                        this.getX(), this.getY(), this.getZ(),
                        1,
                        0.0, 0.0, 0.0, 0.0
                );
                this.playSound(
                        SoundEvents.GENERIC_EXPLODE.value(),
                        0.7F,
                        1.6F + (Level.getRandom().nextFloat() - Level.getRandom().nextFloat()) * 0.4F
                );
                source.addRuntimeTag(DamageTypeTags.BYPASSES_ARMOR)
                      .addRuntimeTag(DamageTypeTags.BYPASSES_EFFECTS)
                      .addRuntimeTag(DamageTypeTags.BYPASSES_COOLDOWN)
                      .addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS);
                entity.hurt(source, (float) this.getDamage());
            }
        }
    }
}
