package net.METS_Blade.Client;

import net.METS_Blade.METS_SlashBlade;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
@EventBusSubscriber(modid = METS_SlashBlade.MODID, value = Dist.CLIENT)
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
