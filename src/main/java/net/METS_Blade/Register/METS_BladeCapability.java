package net.METS_Blade.Register;

import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
public class METS_BladeCapability {
    public static void RegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, ctx) -> new METS_FEBladeStorage(stack),
                METS_BladeItem.ITEMS.getEntries().stream().map(DeferredHolder::get).toArray(Item[]::new)
        );
    }
}
