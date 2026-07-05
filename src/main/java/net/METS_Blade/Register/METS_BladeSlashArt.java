package net.METS_Blade.Register;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.METS_Blade.METS_SlashBlade;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
public class METS_BladeSlashArt {
    public static final DeferredRegister<SlashArts> SLASH_ARTS;
    public static final DeferredHolder<SlashArts, SlashArts> HyperSystem;
    public static final DeferredHolder<SlashArts, SlashArts> HyperSpeedSword_Y;
    public static final DeferredHolder<SlashArts, SlashArts> HyperEnergyParticleFlow;
    public static final DeferredHolder<SlashArts, SlashArts> ConservationEnergy;
    static {
        SLASH_ARTS = DeferredRegister.create(SlashArts.REGISTRY_KEY, METS_SlashBlade.MODID);
        HyperSystem = SLASH_ARTS.register("hyper_system", () -> new SlashArts((e) -> METS_BladeCombo.BloodInnovationCombo.getId()));
        HyperSpeedSword_Y = SLASH_ARTS.register("hyper_speed_sword_y", () -> new SlashArts((e) -> METS_BladeCombo.CraftRenaissanceCombo.getId()));
        HyperEnergyParticleFlow = SLASH_ARTS.register("hyper_energy_particle_flow", () -> new SlashArts((e) -> METS_BladeCombo.TechnicalInnovationCombo.getId()));
        ConservationEnergy = SLASH_ARTS.register("conservation_energy", () -> new SlashArts((e) -> METS_BladeCombo.ConservationEnergyCombo.getId()));
    }
}
