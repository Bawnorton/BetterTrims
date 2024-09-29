package com.bawnorton.bettertrims.networking.packet.s2c;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.entity.effect.StatusEffect;

//? if <1.21 {
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record StatusEffectDurationModifiedS2CPacket(StatusEffect effect, int modifiedTime) implements FabricPacket {
    public static final PacketType<StatusEffectDurationModifiedS2CPacket> TYPE = PacketType.create(
            BetterTrims.id(StatusEffectDurationModifiedS2CPacket.class.getSimpleName().toLowerCase()),
            StatusEffectDurationModifiedS2CPacket::new
    );

    private StatusEffectDurationModifiedS2CPacket(PacketByteBuf buf) {
        this(StatusEffect.byRawId(buf.readInt()), buf.readInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(StatusEffect.getRawId(effect));
    }

    @Override
    public PacketType<StatusEffectDurationModifiedS2CPacket> getType() {
        return TYPE;
    }
}
//?} else {

/*import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

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
*///?}