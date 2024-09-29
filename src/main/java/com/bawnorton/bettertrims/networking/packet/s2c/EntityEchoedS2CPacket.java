package com.bawnorton.bettertrims.networking.packet.s2c;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.EchoShardTrimEffect;
import net.minecraft.util.math.Vec3d;

//? if <1.21 {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record EntityEchoedS2CPacket(Vec3d priorPos, EchoShardTrimEffect.Echo echo) implements FabricPacket {
    public static final PacketType<EntityEchoedS2CPacket> TYPE = PacketType.create(
            BetterTrims.id(EntityEchoedS2CPacket.class.getSimpleName().toLowerCase()),
            EntityEchoedS2CPacket::new
    );

    private EntityEchoedS2CPacket(PacketByteBuf buf) {
        this(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), EchoShardTrimEffect.Echo.fromBuf(buf));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeDouble(priorPos.x);
        buf.writeDouble(priorPos.y);
        buf.writeDouble(priorPos.z);
        echo.writeBuf(buf);
    }

    @Override
    public PacketType<EntityEchoedS2CPacket> getType() {
        return TYPE;
    }
}
*///?} else {
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

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
//?}