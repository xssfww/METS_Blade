package net.METS_Blade.SlashBlade.Entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import java.util.List;
public class EntityCraftSword extends EntityCommonSword {
    private final int[] color = new int[] { 16711680, 16776960, 255, 65280, 65535, 41215, 9072383 };
    private int colorIndex = 0;
    private int colorTickCounter = 0;
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
            if (this.level() instanceof ServerLevel level) {
                if (this.getShooter() != null) {
                    List<DamageSource> SourceList = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).holders().map(type -> new DamageSource(type, this, this.getShooter())).toList();
                    float Amount = (float) ((entity.getMaxHealth() * 0.5F + this.getDamage()) / SourceList.size());
                    for (DamageSource Source : SourceList) {
                        entity.hurt(Source, Amount);
                        entity.invulnerableTime = 0;
                    }
                }
            }
        }
    }
}

