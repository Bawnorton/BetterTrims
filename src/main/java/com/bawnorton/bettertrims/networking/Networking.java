package com.bawnorton.bettertrims.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class Networking {
    public static void init() {
    }

    public static void sendToNearby(ServerWorld world, BlockPos pos, CustomPayload payload) {
        world.getPlayers(player -> player.squaredDistanceTo(Vec3d.ofCenter(pos)) <= 64.0D).forEach(player -> ServerPlayNetworking.send(player, payload));
    }
}
