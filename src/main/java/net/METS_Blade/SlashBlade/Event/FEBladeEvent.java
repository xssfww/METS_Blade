package net.METS_Blade.SlashBlade.Event;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
@EventBusSubscriber
public class FEBladeEvent {
    @SubscribeEvent
    public static void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
        IEnergyStorage energyStorage = event.getBlade().getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage instanceof FEBladeStorage bladeFE) {
            if (event.getUser() instanceof ServerPlayer player) {
                if (player.isCreative()) {
                    return;
                }
                if (bladeFE.extractEnergy(bladeFE.getStandbyExtract(), true) == bladeFE.getStandbyExtract()) {
                    bladeFE.extractEnergy(bladeFE.getStandbyExtract(), false);
                }
            }
        }
    }
}
