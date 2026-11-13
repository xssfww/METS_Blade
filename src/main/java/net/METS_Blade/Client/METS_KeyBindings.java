package net.METS_Blade.Client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class METS_KeyBindings {
    public static final KeyMapping TOGGLE_SE = new KeyMapping(
            "key.mets_blade.toggle_se",
            GLFW.GLFW_KEY_K,
            "key.category.mets_blade"
    );
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_SE);
    }
}
