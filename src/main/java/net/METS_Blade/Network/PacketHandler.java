package net.METS_Blade.Network;

import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.Network.Packet.SETogglePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
public class PacketHandler {
    protected static final String PROTOCOL_VERSION = "1";
    protected static int index;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(METS_SlashBlade.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );
    public static synchronized void RegisterManager() {
        Register(SETogglePacket.class, SETogglePacket::encode, SETogglePacket::decode, SETogglePacket::handle);
    }
    protected static <MSG> void Register(final Class<MSG> packet, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle) {
        INSTANCE.messageBuilder(packet, index++).encoder(encoder).decoder(decoder).consumerMainThread(handle).add();
    }
}
