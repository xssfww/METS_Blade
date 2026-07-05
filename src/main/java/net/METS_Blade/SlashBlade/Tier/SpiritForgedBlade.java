package net.METS_Blade.SlashBlade.Tier;

import cn.mmf.energyblade.Energyblade;
import cn.mmf.energyblade.item.ItemFEBlade;
import net.minecraft.world.item.Tier;
public class SpiritForgedBlade extends ItemFEBlade {
    public SpiritForgedBlade(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder
                .component(
                        Energyblade.ENERGY_BLADE_DATA.get(),
                        new Energyblade.EnergyBladeData(
                                0,
                                (int) 5e6,
                                (int) 1e6,
                                (int) 1e5,
                                (int) 1e3,
                                (int) 1e2,
                                true,
                                false
                        )
                )
        );
    }
}
