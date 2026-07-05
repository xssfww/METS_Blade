package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.METS_Blade.SlashBlade.DamageSource.MixDamageSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
public class EntityCraftSword extends EntityCommonSword {
    protected final int[] color = new int[] { 16711680, 16776960, 255, 65280, 65535, 41215, 9072383 };
    protected int colorIndex = 0;
    protected int colorTickCounter = 0;
    public EntityCraftSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            colorTickCounter++;
            if (colorTickCounter >= 2) {
                colorTickCounter = 0;
                setColor(color[colorIndex]);
                colorIndex = (colorIndex + 1) % color.length;
            }
        }
    }
    @Override
    public void rideTick() {
        super.rideTick();
    }
    @Override
    protected void onHitBlock(BlockHitResult blockHitresult) {
        super.onHitBlock(blockHitresult);
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
                        damageTypes.getHolderOrThrow(DamageTypes.OUTSIDE_BORDER), this, this.getShooter(), this.position(), true
                );
                float amount = (float) (entity.getMaxHealth() * 0.5F + this.getDamage());
                entity.hurt(source, amount);
            }
        }
    }
}

