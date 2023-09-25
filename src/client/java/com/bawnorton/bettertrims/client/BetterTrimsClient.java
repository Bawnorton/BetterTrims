package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.event.EventHandler;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrimsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterTrims.MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Initializing Better Trims Client");
        ClientNetworking.init();
        EventHandler.init();
    }
}