package net.METS_Blade.Register;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.SlashBlade.SE.BloodInnovationSE;
import net.METS_Blade.SlashBlade.SE.CraftRenaissanceSE;
import net.METS_Blade.SlashBlade.SE.TechnicalInnovationSE;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class METS_BladeSlashEffect {
    public static final DeferredRegister<SpecialEffect> SPECIAL_EFFECT;
    public static final DeferredHolder<SpecialEffect, SpecialEffect> Divine_Surge_Fierce_Wave;
    public static final DeferredHolder<SpecialEffect, SpecialEffect> Divine_Surge_Fire_Arrow;
    public static final DeferredHolder<SpecialEffect, SpecialEffect> Doom_Surge_Curse_Destroy;
    public static final DeferredHolder<SpecialEffect, SpecialEffect> Inch_Surge_Arrow;
    static {
        SPECIAL_EFFECT = DeferredRegister.create(SpecialEffect.REGISTRY_KEY, METS_SlashBlade.MODID);
        Divine_Surge_Fierce_Wave = SPECIAL_EFFECT.register("divine_surge_fierce_wave", BloodInnovationSE::new);
        Divine_Surge_Fire_Arrow = SPECIAL_EFFECT.register("divine_surge_fire_arrow", CraftRenaissanceSE::new);
        Doom_Surge_Curse_Destroy = SPECIAL_EFFECT.register("doom_surge_curse_destroy", TechnicalInnovationSE::new);
        Inch_Surge_Arrow = SPECIAL_EFFECT.register("inch_surge_arrow", CraftRenaissanceSE::new);
    }
}
