package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.METS_Blade.SlashBlade.DamageSource.MixDamageSource;
import net.METS_Blade.SlashBlade.Util.ToolManager;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import java.util.List;
public class EntityCraftSword extends EntityCommonSword {
    protected final int[] color = new int[] { 16711680, 16776960, 255, 65280, 65535, 41215, 9072383 };
    protected int colorIndex = 0;
    protected int colorTickCounter = 0;
    public EntityCraftSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    public static EntityCraftSword createInstance(Level worldIn) {
        return new EntityCraftSword(SlashBlade.RegistryEvents.SummonedSword, worldIn);
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
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.getShooter() != null && this.level() instanceof ServerLevel level) {
            MixDamageSource source = createBorderDamageSource(level);
            List<Entity> entityList = ToolManager.getEntity(level, this, 3);
            entityList.forEach(entity -> entity.hurt(source, calculateDamage(entity)));
            level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.playSound(SoundEvents.GENERIC_EXPLODE, 0.7F, 1.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.4F);
        }
        super.burst();
    }
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        if (this.getShooter() != null && this.level() instanceof ServerLevel serverLevel) {
            MixDamageSource source = createBorderDamageSource(serverLevel);
            if (targetEntity instanceof LivingEntity livingEntity) {
                KnockBacks.cancel.action.accept(livingEntity);
                StunManager.setStun(livingEntity);
            }
            targetEntity.hurt(source, calculateDamage(targetEntity));
        }
    }
    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        if (target instanceof Player player) {
            return !player.isSpectator();
        }
        return true;
    }
    protected MixDamageSource createBorderDamageSource(ServerLevel serverLevel) {
        Registry<DamageType> damageTypes = serverLevel.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE);
        return new MixDamageSource(
                damageTypes.getHolderOrThrow(DamageTypes.OUTSIDE_BORDER),
                this,
                this.getShooter(),
                this.position(),
                true
        );
    }
    protected float calculateDamage(Entity target) {
        if (target instanceof LivingEntity living) {
            return (float) (living.getMaxHealth() * 0.5F + this.getDamage());
        }
        return (float) this.getDamage();
    }
}