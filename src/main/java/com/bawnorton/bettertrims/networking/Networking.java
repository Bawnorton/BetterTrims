package com.bawnorton.bettertrims.networking;

import com.bawnorton.bettertrims.networking.packet.c2s.MagnetToggleC2SPacket;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

//? if <1.21 {
import net.minecraft.server.network.ServerPlayerEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

public final class Networking {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(MagnetToggleC2SPacket.TYPE, Networking::handleMagnetToggle);
    }

    private static void handleMagnetToggle(MagnetToggleC2SPacket packet, ServerPlayerEntity player, PacketSender packetSender) {
        boolean enabled = packet.enabled();
        TrimEffects.IRON.setMagnetEnabled(player, enabled);
        Text message = Text.translatable("bettertrims.item_magnet.notification.magnet");
        Text toggle = Text.translatable("bettertrims.item_magnet.notification.magnet.%s".formatted(enabled)).styled(style -> style.withColor(enabled ? 0xFF00FF00 : 0xFFDF5050));
        message.getSiblings().add(toggle);
        player.sendMessage(message, true);
    }
}
//?} else {
/*import com.bawnorton.bettertrims.networking.packet.s2c.StatusEffectDurationModifiedS2CPacket;
import com.bawnorton.bettertrims.networking.packet.s2c.EchoTriggeredS2CPacket;
import com.bawnorton.bettertrims.networking.packet.s2c.EntityEchoedS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.util.Colors;

public final class Networking {
    public static void init() {
        PayloadTypeRegistry<RegistryByteBuf> playC2S = PayloadTypeRegistry.playC2S();
        playC2S.register(MagnetToggleC2SPacket.PACKET_ID, MagnetToggleC2SPacket.PACKET_CODEC);

        PayloadTypeRegistry<RegistryByteBuf> playS2C = PayloadTypeRegistry.playS2C();
        playS2C.register(StatusEffectDurationModifiedS2CPacket.PACKET_ID, StatusEffectDurationModifiedS2CPacket.PACKET_CODEC);
        playS2C.register(EchoTriggeredS2CPacket.PACKET_ID, EchoTriggeredS2CPacket.PACKET_CODEC);
        playS2C.register(EntityEchoedS2CPacket.PACKET_ID, EntityEchoedS2CPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MagnetToggleC2SPacket.PACKET_ID, Networking::handleMagnetToggle);
    }

    private static void handleMagnetToggle(MagnetToggleC2SPacket packet, ServerPlayNetworking.Context context) {
        boolean enabled = packet.enabled();
        TrimEffects.IRON.setMagnetEnabled(context.player(), enabled);
        Text message = Text.translatable("bettertrims.item_magnet.notification.magnet");
        Text toggle = Text.translatable("bettertrims.item_magnet.notification.magnet.%s".formatted(enabled)).withColor(enabled ? Colors.GREEN : Colors.LIGHT_RED);
        message.getSiblings().add(toggle);
        context.player().sendMessage(message, true);
    }
}
*///?}
