package net.METS_Blade;

import com.mojang.logging.LogUtils;
import net.METS_Blade.Register.METS_BladeCombo;
import net.METS_Blade.Register.METS_BladeItem;
import net.METS_Blade.Register.METS_BladeSlashArt;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
@Mod(METS_SlashBlade.MODID)
public class METS_SlashBlade {
    public static final String MODID = "mets_blade";
    public static final Logger LOGGER = LogUtils.getLogger();
    public METS_SlashBlade(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        METS_BladeItem.ITEMS.register(bus);
        METS_BladeCombo.COMBO_STATES.register(bus);
        METS_BladeSlashArt.SLASH_ARTS.register(bus);
        METS_BladeSlashEffect.SPECIAL_EFFECT.register(bus);
    }
}
