package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.event.EventHandler;
import com.bawnorton.bettertrims.networking.Networking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrims implements ModInitializer {
    public static final String MOD_ID = "bettertrims";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.debug("Initializing Better Trims");
        Networking.init();
        EventHandler.init();
    }
}

