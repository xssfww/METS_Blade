package net.METS_Blade.SlashBlade.SE;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber
public class TechnicalInnovationSE  extends SpecialEffect {
    public TechnicalInnovationSE() {
        super(25, true, true);
    }
    @SubscribeEvent
    public static void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(METS_BladeSlashEffect.Doom_Surge_Curse_Destroy.getId())) {
            LivingEntity user = event.getUser();
            if (user instanceof ServerPlayer player) {
                if ((player.level() instanceof ServerLevel)) {
                    int level = player.experienceLevel;
                    if (SpecialEffect.isEffective(METS_BladeSlashEffect.Doom_Surge_Curse_Destroy.get(), level)) {
                        if (event.getTarget() != null) {
                            doTechnicalInnovationSE(player, event.getTarget(), false);
                        }
                    }
                }
            }
        }
    }
    public static void doTechnicalInnovationSE(LivingEntity owner, LivingEntity TargetEntity, boolean Simulate) {
        Level world = owner.level();
        if (world instanceof ServerLevel level) {
            ItemStack stack = owner.getMainHandItem();
            boolean hasEnergy;
            boolean EnergyCheck = stack.getCapability(ForgeCapabilities.ENERGY)
                    .filter(FEBladeStorage.class::isInstance)
                    .map(FEBladeStorage.class::cast)
                    .map(energy -> energy.extractEnergy((int) 1e4, false) > 0)
                    .orElse(false);
            if (owner instanceof ServerPlayer player) {
                if (player.isCreative()) {
                    hasEnergy = true;
                } else {
                    hasEnergy = EnergyCheck;
                }
            } else {
                hasEnergy = EnergyCheck;
            }
            if (!Simulate) {
                if (level.getRandom().nextBoolean()) {
                    if (!hasEnergy) {
                        return;
                    }
                }
            }
            TargetEntity.hurt(owner.damageSources().magic(),TargetEntity.getMaxHealth() / 3.0F);
        }
    }
}
