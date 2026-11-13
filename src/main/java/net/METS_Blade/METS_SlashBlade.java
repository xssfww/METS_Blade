package net.METS_Blade;

import net.METS_Blade.Network.PacketHandler;
import net.METS_Blade.Register.METS_BladeCombo;
import net.METS_Blade.Register.METS_BladeItem;
import net.METS_Blade.Register.METS_BladeSlashArt;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod("mets_blade")
public class METS_SlashBlade {
    public static final String MODID = "mets_blade";
    public METS_SlashBlade() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        METS_BladeItem.ITEMS.register(bus);
        METS_BladeCombo.COMBO_STATES.register(bus);
        METS_BladeSlashArt.SLASH_ARTS.register(bus);
        METS_BladeSlashEffect.SPECIAL_EFFECT.register(bus);
        PacketHandler.RegisterManager();
    }
}
