package net.METS_Blade.SlashBlade.SE;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber
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
                if ((player.level() instanceof ServerLevel)) {
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
            int rank = owner.getCapability(CapabilityConcentrationRank.RANK_POINT).map(r -> r.getRank(level.getGameTime()).level).orElse(0);
            float damage = 5.0F * rank;
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
