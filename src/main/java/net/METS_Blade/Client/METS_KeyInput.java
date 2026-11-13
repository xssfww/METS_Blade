package net.METS_Blade.Client;

import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.Network.Packet.SETogglePacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
@EventBusSubscriber(modid = METS_SlashBlade.MODID, value = Dist.CLIENT)
public class METS_KeyInput {
    @SubscribeEvent
    public static void onInput(InputEvent.Key event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (event.getKey() == METS_KeyBindings.TOGGLE_SE.getKey().getValue()) {
                if (Minecraft.getInstance().player != null) {
                    PacketDistributor.sendToServer(new SETogglePacket());
                }
            }
        }
    }
}
