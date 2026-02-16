package net.METS_Blade.Capability;

import cn.mmf.energyblade.energy.FEBladeStorage;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
@AutoRegisterCapability
public class METS_FEBladeStorage extends FEBladeStorage {
    public METS_FEBladeStorage(int energy, int capacity,int maxReceive,int maxExtract, int powerupExtract, int standbyExtract, boolean energyDurability) {
        super(energy,capacity,powerupExtract,standbyExtract,energyDurability);
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }
    public int getMaxReceive() {
        return this.maxReceive;
    }
    public int getMaxExtract() {
        return this.maxExtract;
    }
    public void setEnergy(int NewEnergy) {
        this.energy = Math.min(this.capacity, Math.max(0, NewEnergy));
    }
}
