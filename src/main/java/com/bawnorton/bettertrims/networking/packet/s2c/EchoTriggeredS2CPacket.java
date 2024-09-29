package com.bawnorton.bettertrims.networking.packet.s2c;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.EchoShardTrimEffect;

//? if <1.21 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record EchoTriggeredS2CPacket(EchoShardTrimEffect.Echo echo) implements FabricPacket {
    public static final PacketType<EchoTriggeredS2CPacket> TYPE = PacketType.create(
            BetterTrims.id(EchoTriggeredS2CPacket.class.getSimpleName().toLowerCase()),
            EchoTriggeredS2CPacket::new
    );

    private EchoTriggeredS2CPacket(PacketByteBuf buf) {
        this(EchoShardTrimEffect.Echo.fromBuf(buf));
    }

    @Override
    public void write(PacketByteBuf buf) {
        echo.writeBuf(buf);
    }

    @Override
    public PacketType<EchoTriggeredS2CPacket> getType() {
        return TYPE;
    }
}
*///?} else {
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record EchoTriggeredS2CPacket(EchoShardTrimEffect.Echo echo) implements CustomPayload {
    public static final Id<EchoTriggeredS2CPacket> PACKET_ID = new Id<>(BetterTrims.id(EchoTriggeredS2CPacket.class.getSimpleName().toLowerCase()));
    public static final PacketCodec<ByteBuf, EchoTriggeredS2CPacket> PACKET_CODEC = EchoShardTrimEffect.Echo.PACKET_CODEC.xmap(EchoTriggeredS2CPacket::new, EchoTriggeredS2CPacket::echo);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
//?}