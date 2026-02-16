package net.METS_Blade.Register;

import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.METS_Blade.SlashBlade.Tier.FirstForgedBlade;
import net.METS_Blade.SlashBlade.Tier.GodForgedBlade;
import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.SlashBlade.Tier.NanoForgedBlade;
import net.METS_Blade.SlashBlade.Tier.SpiritForgedBlade;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class METS_BladeItem {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> GodForged_Bloody;
    public static final RegistryObject<Item> GodForged_Craft;
    public static final RegistryObject<Item> SpiritForged_Technical;
    public static final RegistryObject<Item> SpiritForged_Conservation;
    public static final RegistryObject<Item> NanoForged;
    public static final RegistryObject<Item> FirstForged;
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, METS_SlashBlade.MODID);
        GodForged_Bloody = ITEMS.register("god_forged_blade_bloody", () -> new
                GodForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
        GodForged_Craft = ITEMS.register("god_forged_blade_craft", () -> new
                GodForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
        SpiritForged_Technical = ITEMS.register("spirit_forged_blade_technical", () -> new
                SpiritForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
        SpiritForged_Conservation = ITEMS.register("spirit_forged_blade_conservation", () -> new
                SpiritForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
        NanoForged = ITEMS.register("nano_forged_blade", () -> new
                NanoForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
        FirstForged = ITEMS.register("first_forged_blade", () -> new
                FirstForgedBlade(new ItemTierSlashBlade(40, 4.0F), 4, -2.4F, new Item.Properties()));
    }
}
