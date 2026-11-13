package net.METS_Blade.SlashBlade.SE;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.METS_Blade.SlashBlade.Util.SEBladeSwitch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
@EventBusSubscriber
public class BloodInnovationSE extends SpecialEffect {
    public BloodInnovationSE() {
        super(35, true, true);
    }
    @SubscribeEvent
    public static void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(METS_BladeSlashEffect.Divine_Surge_Fierce_Wave.getId())) {
            LivingEntity user = event.getUser();
            if (user instanceof ServerPlayer player) {
                if (SEBladeSwitch.isDisabled(player)) {
                    return;
                }
                if (player.level() instanceof ServerLevel) {
                    int level = player.experienceLevel;
                    if (SpecialEffect.isEffective(METS_BladeSlashEffect.Divine_Surge_Fierce_Wave.get(), level)) {
                        if (event.getTarget() != null) {
                            doBloodInnovationSE(player,event.getTarget().position(),false);
                        }
                    }
                }
            }
        }
    }
    public static void doBloodInnovationSE(LivingEntity owner, Vec3 TargetPos,boolean Simulate) {
        Level world = owner.level();
        if (world instanceof ServerLevel level) {
            ItemStack stack = owner.getMainHandItem();
            int rank = owner.getData(CapabilityConcentrationRank.RANK_POINT).getRank(level.getGameTime()).level;
            float damage = 5.0F * rank;
            boolean hasEnergy;
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage instanceof METS_FEBladeStorage FEBlade) {
                boolean EnergyCheck = FEBlade.extractEnergy((int) 1e4, false) > 0;
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
                    if (!hasEnergy) {
                        return;
                    }
                }
                owner.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 150, 3));
                owner.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 3));
                level.explode(owner, TargetPos.x, TargetPos.y, TargetPos.z, damage, Level.ExplosionInteraction.NONE);
            }
        }
    }
}
