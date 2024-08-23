package com.bawnorton.bettertrims.client.networking;

import com.bawnorton.bettertrims.extend.ModifiedTimeHolder;
import com.bawnorton.bettertrims.networking.packet.s2c.StatusEffectDurationModifiedS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.RegistryByteBuf;

public final class ClientNetworking {
    public static void init() {
        PayloadTypeRegistry<RegistryByteBuf> playS2C = PayloadTypeRegistry.playS2C();
        playS2C.register(StatusEffectDurationModifiedS2CPacket.PACKET_ID, StatusEffectDurationModifiedS2CPacket.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(StatusEffectDurationModifiedS2CPacket.PACKET_ID, ClientNetworking::handleStatusEffectDurationModified);
    }

    private static void handleStatusEffectDurationModified(StatusEffectDurationModifiedS2CPacket packet, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
        StatusEffectInstance effectInstance = player.getStatusEffect(packet.effect());
        if(effectInstance == null) return;

        ((ModifiedTimeHolder) effectInstance).bettertrims$setModifiedTime(packet.modifiedTime());
    }
}
