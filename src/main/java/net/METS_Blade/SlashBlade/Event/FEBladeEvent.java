package net.METS_Blade.SlashBlade.Event;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber
public class FEBladeEvent {
    @SubscribeEvent
    public static void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
        event.getBlade().getCapability(ForgeCapabilities.ENERGY).ifPresent((energy) -> {
            if (energy instanceof FEBladeStorage bladeFE) {
                if (event.getUser() instanceof ServerPlayer player) {
                    if (player.isCreative()) {
                        return;
                    }
                    if (bladeFE.extractEnergy(bladeFE.getStandbyExtract(), true) == bladeFE.getStandbyExtract()) {
                        bladeFE.extractEnergy(bladeFE.getStandbyExtract(), false);
                    }
                }
            }
        });
    }
}
