package com.bawnorton.bettertrims.client.networking;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.networking.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public abstract class ClientNetworking {
    public static void init() {
        ClientPlayNetworking.registerReceiver(Networking.CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            String serialized = buf.readString();
            ConfigManager.deserializeConfig(serialized);
        });
        ClientPlayNetworking.registerReceiver(Networking.CONFIG_OP_CHECK, (client, handler, buf, responseSender) -> {
            boolean canSendConfig = buf.readBoolean();
            if (canSendConfig) sendConfigToServer();
        });
    }

    public static void sendHandshake() {
        ClientPlayNetworking.send(Networking.HANDSHAKE, PacketByteBufs.empty());
    }

    public static void trySendConfigToServer() {
        ClientPlayNetworking.send(Networking.CONFIG_OP_CHECK, PacketByteBufs.empty());
    }

    public static void sendConfigToServer() {
        ClientPlayNetworking.send(Networking.CONFIG_SYNC, PacketByteBufs.create().writeString(ConfigManager.serializeConfig()));
    }
}
