package com.bawnorton.bettertrims.networking.packet.s2c;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public record StatusEffectDurationModifiedS2CPacket(RegistryEntry<StatusEffect> effect, int modifiedTime) implements CustomPayload {
    public static final Id<StatusEffectDurationModifiedS2CPacket> PACKET_ID = new Id<>(BetterTrims.id(StatusEffectDurationModifiedS2CPacket.class.getSimpleName().toLowerCase()));
    public static final PacketCodec<RegistryByteBuf, StatusEffectDurationModifiedS2CPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntry(RegistryKeys.STATUS_EFFECT),
            StatusEffectDurationModifiedS2CPacket::effect,
            PacketCodecs.VAR_INT,
            StatusEffectDurationModifiedS2CPacket::modifiedTime,
            StatusEffectDurationModifiedS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
