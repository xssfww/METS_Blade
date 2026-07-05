package net.METS_Blade.Mixin;

import cn.mmf.energyblade.Energyblade;
import cn.mmf.energyblade.item.ItemFEBlade;
import mods.flammpfeil.slashblade.recipe.SlashBladeShapedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(SlashBladeShapedRecipe.class)
public abstract class SlashBladeRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At("TAIL"), cancellable = true)
    private void onAssemble(CraftingInput input, HolderLookup.Provider access, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result.isEmpty() || !(result.getItem() instanceof ItemFEBlade)) return;
        Energyblade.EnergyBladeData defaultData = result.getOrDefault(
                Energyblade.ENERGY_BLADE_DATA.get(),
                Energyblade.EnergyBladeData.DEFAULT
        );
        int totalEnergy = 0;
        for (ItemStack stack : input.items()) {
            if (!stack.isEmpty()) {
                IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (storage != null) {
                    totalEnergy += storage.getEnergyStored();
                }
            }
        }
        int newEnergy = Math.min(totalEnergy, defaultData.capacity());
        Energyblade.EnergyBladeData newData = new Energyblade.EnergyBladeData(
                newEnergy,
                defaultData.capacity(),
                defaultData.maxReceive(),
                defaultData.maxExtract(),
                defaultData.powerupExtract(),
                defaultData.standbyExtract(),
                defaultData.energyDurability(),
                false
        );
        result.set(Energyblade.ENERGY_BLADE_DATA.get(), newData);
        cir.setReturnValue(result);
    }
}
