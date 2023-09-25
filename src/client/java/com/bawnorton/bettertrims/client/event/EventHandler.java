package com.bawnorton.bettertrims.client.event;

import com.bawnorton.bettertrims.client.impl.YACLImpl;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public abstract class EventHandler {
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> ClientNetworking.sendHandshake());
    }

    public static void onRecievedConfig() {
        YACLImpl.refreshScreen();
    }
}
