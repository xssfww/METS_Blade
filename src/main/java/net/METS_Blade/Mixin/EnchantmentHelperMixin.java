package net.METS_Blade.Mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mods.flammpfeil.slashblade.util.EnchantmentsHelper;
import net.METS_Blade.Register.METS_BladeItem;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = EnchantmentsHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "updateEnchantment", at = @At("HEAD"), cancellable = true)
    private static void customUpdateEnchantment(ItemStack result, ItemStack ingredient, HolderLookup.Provider access, CallbackInfo ci) {
        if (result.is(METS_BladeItem.GodForged_Bloody.get()) || result.is(METS_BladeItem.GodForged_Craft.get())) {
            HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = access.lookupOrThrow(Registries.ENCHANTMENT);
            ItemEnchantments.Mutable newEnchants = new ItemEnchantments.Mutable(result.getAllEnchantments(enchantmentLookup));
            ItemEnchantments oldEnchants = ingredient.getAllEnchantments(enchantmentLookup);
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : oldEnchants.entrySet()) {
                Holder<Enchantment> enchantment = entry.getKey();
                int oldLevel = entry.getIntValue();
                int currentLevel = newEnchants.getLevel(enchantment);
                int mergedLevel = Math.max(oldLevel, currentLevel);
                newEnchants.set(enchantment, mergedLevel);
            }
            EnchantmentHelper.setEnchantments(result, newEnchants.toImmutable());
            ci.cancel();
        }
    }
}
