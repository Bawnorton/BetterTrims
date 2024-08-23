package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.keybind.KeybindManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;

public final class BetterTrimsClient {
    public static void init() {
        BetterTrims.LOGGER.debug( "{} Client Initialized", BetterTrims.MOD_ID);

        ClientNetworking.init();
        KeybindManager.init();
    }
}
