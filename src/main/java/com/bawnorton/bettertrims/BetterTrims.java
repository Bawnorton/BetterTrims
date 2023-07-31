package com.bawnorton.bettertrims;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrims implements ModInitializer {
    public static final String MOD_ID = "bettertrims";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.debug("Initializing Better Trims");
    }
}