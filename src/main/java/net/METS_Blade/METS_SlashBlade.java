package net.METS_Blade;

import net.METS_Blade.Register.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
@Mod("mets_blade")
public class METS_SlashBlade {
    public static final String MODID = "mets_blade";
    public METS_SlashBlade(IEventBus bus, ModContainer container) {
        METS_BladeItem.ITEMS.register(bus);
        METS_BladeCombo.COMBO_STATES.register(bus);
        METS_BladeSlashArt.SLASH_ARTS.register(bus);
        METS_BladeSlashEffect.SPECIAL_EFFECT.register(bus);
        bus.addListener(METS_BladeCapability::RegisterCapabilities);
    }
}
