package net.METS_Blade.SlashBlade.Client;

import cn.mmf.energyblade.client.render.EnergyBladeBEWLR;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.ClientHandler;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.Register.METS_BladeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
@EventBusSubscriber(value = Dist.CLIENT, modid = METS_SlashBlade.MODID)
public class ModelHandler {
    @SubscribeEvent
    public static void setModelUser(FMLClientSetupEvent event) {
        METS_BladeItem.ITEMS.getEntries().forEach((blade) -> {
            if (blade.get() instanceof ItemSlashBlade) {
                ItemProperties.register(blade.get(), SlashBlade.prefix("user"), (stack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0.0F;
                });
            }
        });
    }
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(METS_BladeEntity.CommonSwords, SummonedSwordRenderer::new);
        event.registerEntityRenderer(METS_BladeEntity.BloodSwords, SummonedSwordRenderer::new);
        event.registerEntityRenderer(METS_BladeEntity.CraftSwords, SummonedSwordRenderer::new);
    }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        METS_BladeItem.ITEMS.getEntries().forEach((blade) -> {
            if (blade.get() instanceof ItemSlashBlade) {
                event.registerItem(new IClientItemExtensions() {
                    private final EnergyBladeBEWLR renderer = new EnergyBladeBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return this.renderer;
                    }
                }, blade.get());
            }
        });
    }
    @SubscribeEvent
    public static void baked(ModelEvent.ModifyBakingResult event) {
        METS_BladeItem.ITEMS.getEntries().forEach((blade) -> {
            if (blade.get() instanceof ItemSlashBlade) {
                ClientHandler.bakeBlade(blade.get(), event);
            }
        });
    }
}
