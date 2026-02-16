package net.METS_Blade.SlashBlade.SA;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.Drive;
import mods.flammpfeil.slashblade.slasharts.SakuraEnd;
import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
public class ConservationEnergySA {
    public static void doConservationEnergySA(LivingEntity owner,boolean simulate) {
        ItemStack stack = owner.getMainHandItem();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                owner.getCapability(CapabilityConcentrationRank.RANK_POINT).ifPresent(Rank -> {
                    int RankLevel = Rank.getRank(Level.getGameTime()).level;
                    float Attack = state.getBaseAttackModifier();
                    if(RankLevel >= 4){
                        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent((energy) -> {
                            if (energy instanceof METS_FEBladeStorage bladeFE) {
                                bladeFE.receiveEnergy(RankLevel * bladeFE.getMaxExtract(),false);
                            }
                        });
                        SakuraEnd.doSlash(owner,owner.getRandom().nextFloat() * 360.0f,Vec3.ZERO,false,false,Attack);
                        Rank.setRawRankPoint(RankLevel/2);
                        owner.playSound(SoundEvents.TRIDENT_THUNDER,1F,1F);
                    } else {
                        boolean hasEnergy;
                        boolean EnergyCheck = stack.getCapability(ForgeCapabilities.ENERGY)
                                .filter(FEBladeStorage.class::isInstance)
                                .map(FEBladeStorage.class::cast)
                                .map(energy -> energy.extractEnergy((int) 1e3, false) > 0)
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
                        if (!simulate) {
                            if (!hasEnergy) {
                                return;
                            }
                        }
                        Drive.doSlash(owner,owner.getRandom().nextFloat() * 360.0f,45, Vec3.ZERO,false,Attack,2.0F);
                    }
                });
            }
        });
    }
}
