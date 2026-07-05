package net.METS_Blade.SlashBlade.Tier;

import cn.mmf.energyblade.Energyblade;
import cn.mmf.energyblade.item.ItemFEBlade;
import net.minecraft.world.item.Tier;
public class FirstForgedBlade extends ItemFEBlade {
    public FirstForgedBlade(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder
                .component(
                        Energyblade.ENERGY_BLADE_DATA.get(),
                        new Energyblade.EnergyBladeData(
                                0,
                                (int) 3e4,
                                (int) 1e4,
                                (int) 1e3,
                                (int) 1e2,
                                (int) 1e1,
                                true,
                                false
                        )
                )
        );
    }
}
