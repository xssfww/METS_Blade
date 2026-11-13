package net.METS_Blade.Network.Packet;

import net.METS_Blade.SlashBlade.Util.SEBladeSwitch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public class SETogglePacket {
    public SETogglePacket() {
    }
    public static void encode(SETogglePacket ignoredPacket, FriendlyByteBuf ignoredBuf) {
    }
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static SETogglePacket decode(FriendlyByteBuf ignoredBuf) {
        return new SETogglePacket();
    }
    public static void handle(SETogglePacket ignoredPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
            context.enqueueWork(() -> {
                boolean enabled = SEBladeSwitch.toggle(player);
                player.displayClientMessage(Component.translatable(enabled ? "message.mets_blade.se_enabled" : "message.mets_blade.se_disabled"), true);
            });
        }
        context.setPacketHandled(true);
    }
}
