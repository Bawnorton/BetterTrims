package com.bawnorton.bettertrims.client.networking;

import com.bawnorton.bettertrims.networking.packet.TrimPatternSourcePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TrimPatternSourcePayload.TYPE, TrimPatternSourcePayload::handle);
    }
}
