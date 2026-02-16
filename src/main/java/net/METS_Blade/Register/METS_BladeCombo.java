package net.METS_Blade.Register;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.SlashBlade.SA.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
public class METS_BladeCombo {
    public static final DeferredRegister<ComboState> COMBO_STATES;
    public static final RegistryObject<ComboState> BloodInnovationCombo;
    public static final RegistryObject<ComboState> CraftRenaissanceCombo;
    public static final RegistryObject<ComboState> TechnicalInnovationCombo;
    public static final RegistryObject<ComboState> ConservationEnergyCombo;
    static {
        COMBO_STATES = DeferredRegister.create(ComboState.REGISTRY_KEY, METS_SlashBlade.MODID);
        ComboState.Builder BloodCombo = ComboState.Builder.newInstance()
                .startAndEnd(1600, 1659)
                .priority(50).motionLoc(DefaultResources.ExMotionLocation)
                .next(ComboState.TimeoutNext.buildFromFrame(15, (entity) ->
                        SlashBlade.prefix("none"))).nextOfTimeout((entity) ->
                        SlashBlade.prefix("drive_vertical_end"))
                .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                        .put(2, (entityIn) ->
                                AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.20999999344348907))
                        .put(3, (entityIn) -> BloodInnovationSA.doBloodInnovationSA(entityIn,false))
                        .build());
        BloodInnovationCombo = COMBO_STATES.register("blood_innovation",BloodCombo::build);
        ComboState.Builder CraftCombo = ComboState.Builder.newInstance()
                .startAndEnd(1600, 1659)
                .priority(50).motionLoc(DefaultResources.ExMotionLocation)
                .next(ComboState.TimeoutNext.buildFromFrame(15, (entity) ->
                        SlashBlade.prefix("none"))).nextOfTimeout((entity) ->
                        SlashBlade.prefix("drive_vertical_end"))
                .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                        .put(2, (entityIn) ->
                                AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.20999999344348907))
                        .put(3, (entityIn) -> CraftRenaissanceSA.doCraftRenaissanceSA(entityIn,false))
                        .build());
        CraftRenaissanceCombo = COMBO_STATES.register("craft_renaissance",CraftCombo::build);
        ComboState.Builder TechnicalCombo = ComboState.Builder.newInstance()
                .startAndEnd(1600, 1659)
                .priority(50).motionLoc(DefaultResources.ExMotionLocation)
                .next(ComboState.TimeoutNext.buildFromFrame(15, (entity) ->
                        SlashBlade.prefix("none"))).nextOfTimeout((entity) ->
                        SlashBlade.prefix("drive_vertical_end"))
                .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                        .put(2, (entityIn) ->
                                AttackManager.doSlash(entityIn, -80.0F, Vec3.ZERO, false, false, 0.20999999344348907))
                        .put(3, (entityIn) -> TechnicalInnovationSA.doTechnicalInnovationSA(entityIn,false))
                        .build());
        TechnicalInnovationCombo = COMBO_STATES.register("technical_innovation",TechnicalCombo::build);
        ComboState.Builder ConservationCombo = ComboState.Builder.newInstance()
                .startAndEnd(204, 218).speed(1.1F).priority(50).next((entity) ->
                        SlashBlade.prefix("none")).nextOfTimeout((entity) ->
                        SlashBlade.prefix("sakura_end_finish")).clickAction((entity) ->
                        ConservationEnergySA.doConservationEnergySA(entity, false))
                .addTickAction(UserPoseOverrider::resetRot)
                .addHitEffect((t, a) -> StunManager.setStun(t, 36L));
        ConservationEnergyCombo = COMBO_STATES.register("conservation_energy",ConservationCombo::build);
    }
}
