package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.keybind.KeybindManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import net.minecraft.text.Text;

public final class BetterTrimsClient {
    public static void init() {
        BetterTrims.LOGGER.debug( "{} Client Initialized", BetterTrims.MOD_ID);

        ClientNetworking.init();
        KeybindManager.init();
    }

    public static Text twoDpFormatter(float value) {
        return Text.literal(String.format("%,.2f", value).replaceAll("[\u00a0\u202F]", " "));
    }
}
