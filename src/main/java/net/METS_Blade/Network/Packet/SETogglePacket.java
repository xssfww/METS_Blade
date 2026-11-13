package net.METS_Blade.Network.Packet;

import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.SlashBlade.Util.SEBladeSwitch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
public class SETogglePacket implements CustomPacketPayload {
    public static final Type<SETogglePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(METS_SlashBlade.MODID, "se_toggle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SETogglePacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
            },
            buf -> new SETogglePacket()
    );
    public SETogglePacket() {
    }
    @Override
    public @NotNull Type<SETogglePacket> type() {
        return TYPE;
    }
    public static void handle(SETogglePacket ignoredPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                boolean enabled = SEBladeSwitch.toggle(player);
                player.displayClientMessage(Component.translatable(enabled ? "message.mets_blade.se_enabled" : "message.mets_blade.se_disabled"), true);
            }
        });
    }
}
