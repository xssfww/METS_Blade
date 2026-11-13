package net.METS_Blade.Network;

import net.METS_Blade.Network.Packet.SETogglePacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
    protected static final String PROTOCOL_VERSION = "1";
    public static void RegisterManager(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(SETogglePacket.TYPE, SETogglePacket.STREAM_CODEC, SETogglePacket::handle);
    }
}
