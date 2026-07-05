package net.METS_Blade.SlashBlade.SA;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
public class ConservationEnergySA {
    public static void doConservationEnergySA(LivingEntity owner,boolean simulate) {
        ItemStack stack = owner.getMainHandItem();
        BladeStateAccess.of(stack).ifPresent(State -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                IConcentrationRank rank = owner.getData(CapabilityConcentrationRank.RANK_POINT);
                int level = owner.getData(CapabilityConcentrationRank.RANK_POINT).getRank(Level.getGameTime()).level;
                float Attack = State.getBaseAttackModifier();
                if (level >= 4) {
                    IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                    if (energyStorage instanceof METS_FEBladeStorage FEBlade) {
                        FEBlade.receiveEnergy(level * FEBlade.maxReceive(), false);
                    }
                    SakuraEnd.doSlash(owner, owner.getRandom().nextFloat() * 360.0f, Vec3.ZERO, false, false, Attack);
                    rank.setRawRankPoint(level / 2);
                    owner.playSound(SoundEvents.TRIDENT_THUNDER.value(), 1F, 1F);
                } else {
                    boolean hasEnergy;
                    IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                    if (energyStorage instanceof METS_FEBladeStorage FEBlade) {
                        boolean EnergyCheck = FEBlade.extractEnergy((int) 1e3, false) > 0;
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
                        Drive.doSlash(owner, owner.getRandom().nextFloat() * 360.0f, 45, Vec3.ZERO, false, Attack, 2.0F);
                    }
                }
            }
        });
    }
}
