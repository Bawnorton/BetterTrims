package com.bawnorton.bettertrims.client.networking;

import com.bawnorton.bettertrims.client.event.EventHandler;
import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.networking.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public abstract class ClientNetworking {
    private static boolean isConnectedToDedicated = false;
    private static boolean displayNewConfigToast = false;
    private static boolean refreshScreen = false;

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(Networking.CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            isConnectedToDedicated = buf.readBoolean();
            String serialized = buf.readString();
            ConfigManager.deserializeConfig(serialized);
            if(!isConnectedToDedicated) return;

            if (displayNewConfigToast) {
                ToastManager toastManager = client.getToastManager();
                toastManager.add(SystemToast.create(client, SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("bettertrims.new_config.title"), Text.translatable("bettertrims.new_config.desc")));
            } else displayNewConfigToast = true;

            if(refreshScreen) {
                client.execute(EventHandler::onRecievedConfig);
                refreshScreen = false;
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(Networking.CONFIG_OP_CHECK, (client, handler, buf, responseSender) -> {
            displayNewConfigToast = false;
            ToastManager toastManager = client.getToastManager();
            boolean canSendConfig = buf.readBoolean();
            if (canSendConfig) {
                sendConfigToServer();
                toastManager.add(SystemToast.create(client, SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("bettertrims.config_synced.title"), Text.translatable("bettertrims.config_synced.desc")));
                return;
            }

            refreshScreen = true;
            requestConfigFromServer();
            toastManager.add(SystemToast.create(client, SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("bettertrims.config_not_synced.title"), Text.translatable("bettertrims.config_not_synced.desc")));
        });
    }

    public static void sendHandshake() {
        ClientPlayNetworking.send(Networking.HANDSHAKE, PacketByteBufs.empty());
    }

    public static void trySendConfigToServer() {
        ClientPlayNetworking.send(Networking.CONFIG_OP_CHECK, PacketByteBufs.empty());
    }

    public static void sendConfigToServer() {
        ClientPlayNetworking.send(Networking.CONFIG_SYNC, PacketByteBufs.create()
                .writeString(ConfigManager.serializeConfig()));
    }

    private static void requestConfigFromServer() {
        sendHandshake();
    }

    public static boolean isConnectedToDedicated() {
        return isConnectedToDedicated;
    }
}
