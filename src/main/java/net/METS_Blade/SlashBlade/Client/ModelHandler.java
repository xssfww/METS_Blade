package net.METS_Blade.SlashBlade.Client;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.ClientHandler;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.Register.METS_BladeItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
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
    public static void baked(ModelEvent.ModifyBakingResult event) {
        METS_BladeItem.ITEMS.getEntries().forEach((blade) -> {
            if (blade.get() instanceof ItemSlashBlade) {
                ClientHandler.bakeBlade(blade.get(), event);
            }
        });
    }
}
