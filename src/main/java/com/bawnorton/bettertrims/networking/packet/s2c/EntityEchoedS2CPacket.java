package com.bawnorton.bettertrims.networking.packet.s2c;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.EchoShardTrimEffect;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record EntityEchoedS2CPacket(Vec3d priorPos, EchoShardTrimEffect.Echo echo) implements CustomPayload {
    public static final Id<EntityEchoedS2CPacket> PACKET_ID = new Id<>(BetterTrims.id(EntityEchoedS2CPacket.class.getSimpleName().toLowerCase()));
    public static final PacketCodec<ByteBuf, EntityEchoedS2CPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VECTOR3F.xmap(Vec3d::new, Vec3d::toVector3f),
            EntityEchoedS2CPacket::priorPos,
            EchoShardTrimEffect.Echo.PACKET_CODEC,
            EntityEchoedS2CPacket::echo,
            EntityEchoedS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
