package net.METS_Blade.Client;

import net.METS_Blade.Network.Packet.SETogglePacket;
import net.METS_Blade.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class METS_KeyInput {
    private METS_KeyInput() {
    }
    @SuppressWarnings("InstantiationOfUtilityClass")
    @SubscribeEvent
    public static void onInput(InputEvent.Key event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (event.getKey() == METS_KeyBindings.TOGGLE_SE.getKey().getValue()) {
                if (Minecraft.getInstance().player != null) {
                    PacketHandler.INSTANCE.sendToServer(new SETogglePacket());
                }
            }
        }
    }
}
