package net.METS_Blade.Mixin;

import cn.mmf.energyblade.item.ItemFEBlade;
import mods.flammpfeil.slashblade.recipe.SlashBladeShapedRecipe;
import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.METS_Blade.Register.METS_BladeItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.*;
@Mixin(value = SlashBladeShapedRecipe.class)
public abstract class SlashBladeRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("TAIL"), cancellable = true)
    private void Assemble(CraftingContainer container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack Result = cir.getReturnValue();
        if (!Result.isEmpty()) {
            if (Result.getItem() instanceof ItemFEBlade) {
                IEnergyStorage ResultEnergy = Result.getCapability(ForgeCapabilities.ENERGY).orElseThrow(NullPointerException::new);
                int TotalEnergy = 0;
                for (ItemStack stack : container.getItems()) {
                    if (!stack.isEmpty()) {
                        TotalEnergy += stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
                    }
                }
                if (ResultEnergy instanceof METS_FEBladeStorage BladeFE) {
                    if (TotalEnergy > 0) {
                        BladeFE.setEnergy(TotalEnergy);
                    }
                    Result.getOrCreateTag().put("Energy", BladeFE.serializeNBT());
                    cir.setReturnValue(Result);
                }
            }
        }
    }
    @Inject(method = "updateEnchantment", cancellable = true, at = @At("HEAD"),remap = false)
    private void UpdateEnchantment(ItemStack Result, ItemStack Ingredient, CallbackInfo ci) {
        if (Result.is(METS_BladeItem.GodForged_Bloody.get()) || Result.is(METS_BladeItem.GodForged_Craft.get())) {
            Map<Enchantment, Integer> NewItemEnchants = Result.getAllEnchantments();
            Map<Enchantment, Integer> OldItemEnchants = Ingredient.getAllEnchantments();
            for (Map.Entry<Enchantment, Integer> entry : OldItemEnchants.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int oldLevel = entry.getValue();
                int newLevel = Math.max(oldLevel, NewItemEnchants.getOrDefault(enchantment, 0));
                NewItemEnchants.put(enchantment, newLevel);
            }
            EnchantmentHelper.setEnchantments(NewItemEnchants, Result);
            ci.cancel();
        }
    }
}
