package com.bawnorton.bettertrims.networking.packet.c2s;

import com.bawnorton.bettertrims.BetterTrims;

//? if <1.21 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record MagnetToggleC2SPacket(boolean enabled) implements FabricPacket {
    public static final PacketType<MagnetToggleC2SPacket> TYPE = PacketType.create(
            BetterTrims.id(MagnetToggleC2SPacket.class.getSimpleName().toLowerCase()),
            MagnetToggleC2SPacket::new
    );

    private MagnetToggleC2SPacket(PacketByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    @Override
    public PacketType<MagnetToggleC2SPacket> getType() {
        return TYPE;
    }
}
*///?} else {
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MagnetToggleC2SPacket(boolean enabled) implements CustomPayload {
    public static final Id<MagnetToggleC2SPacket> PACKET_ID = new Id<>(BetterTrims.id(MagnetToggleC2SPacket.class.getSimpleName().toLowerCase()));
    public static final PacketCodec<ByteBuf, MagnetToggleC2SPacket> PACKET_CODEC = PacketCodecs.BOOL.xmap(MagnetToggleC2SPacket::new, MagnetToggleC2SPacket::enabled);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
//?}
