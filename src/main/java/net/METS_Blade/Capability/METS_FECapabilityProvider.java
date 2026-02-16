package net.METS_Blade.Capability;

import mods.flammpfeil.slashblade.capability.slashblade.NamedBladeStateCapabilityProvider;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class METS_FECapabilityProvider extends NamedBladeStateCapabilityProvider {
    private final LazyOptional<IEnergyStorage> lazyOptional;
    private final METS_FEBladeStorage energyStorage;
    public METS_FECapabilityProvider(ItemStack stack, int energy, int capacity,int maxReceive,int maxExtract, int powerupExtract, int standbyExtract, boolean energyDurability) {
        super(stack);
        this.energyStorage = new METS_FEBladeStorage(energy, capacity, maxReceive, maxExtract, powerupExtract, standbyExtract, energyDurability);
        this.lazyOptional = LazyOptional.of(() -> this.energyStorage);
    }
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.lazyOptional.cast();
        } else {
            return cap == ItemSlashBlade.BLADESTATE ? super.getCapability(cap, side) : LazyOptional.empty();
        }
    }
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("Energy", this.energyStorage.serializeNBT());
        return tag;
    }
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("Energy")) {
            this.energyStorage.deserializeNBT(nbt.getCompound("Energy"));
        }
    }
}
