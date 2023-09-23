package com.bawnorton.bettertrims.client.event;

import com.bawnorton.bettertrims.client.impl.YACLImpl;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.networking.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public abstract class EventHandler {
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> ClientNetworking.sendHandshake());
    }

    public static void onRecievedConfig() {
        YACLImpl.refreshScreen();
    }
}
