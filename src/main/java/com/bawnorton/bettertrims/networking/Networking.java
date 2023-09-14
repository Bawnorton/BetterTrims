package com.bawnorton.bettertrims.networking;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.config.ConfigManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class Networking {
    public static final Identifier CONFIG_OP_CHECK = BetterTrims.id("config_op_check");
    public static final Identifier CONFIG_SYNC = BetterTrims.id("config_sync");
    public static final Identifier HANDSHAKE = BetterTrims.id("handshake");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(HANDSHAKE, (server, player, handler, buf, responseSender) -> sendConfigToPlayer(player));
        ServerPlayNetworking.registerGlobalReceiver(CONFIG_SYNC, (server, player, handler, buf, responseSender) -> {
            ConfigManager.deserializeConfig(buf.readString());
            sendConfigToAllPlayers(server);
        });
        ServerPlayNetworking.registerGlobalReceiver(CONFIG_OP_CHECK, (server, player, handler, buf, responseSender) -> {
            PacketByteBuf response = PacketByteBufs.create();
            response.writeBoolean(player.hasPermissionLevel(2));
            responseSender.sendPacket(CONFIG_OP_CHECK, response);
        });
    }

    private static void sendConfigToAllPlayers(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(Networking::sendConfigToPlayer);
    }

    private static void sendConfigToPlayer(ServerPlayerEntity player) {
        String serialized = ConfigManager.serializeConfig();
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(serialized);
        ServerPlayNetworking.send(player, CONFIG_SYNC, buf);
    }
}
