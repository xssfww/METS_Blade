package net.METS_Blade.Capability;

import cn.mmf.energyblade.Energyblade;
import cn.mmf.energyblade.energy.FEBladeStorage;
import net.minecraft.world.item.ItemStack;
public class METS_FEBladeStorage extends FEBladeStorage {
    private final ItemStack stack;
    public METS_FEBladeStorage(ItemStack stack) {
        super(stack);
        this.stack = stack;
    }
    public Energyblade.EnergyBladeData getData() {
        return this.stack.getOrDefault(Energyblade.ENERGY_BLADE_DATA.get(), Energyblade.EnergyBladeData.DEFAULT);
    }
    public int maxReceive() {
        return this.getData().maxReceive();
    }
}
