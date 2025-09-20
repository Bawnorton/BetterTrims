package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.networking.Networking;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterTrims {
    public static final String MOD_ID = "bettertrims";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        Networking.init();
        LOGGER.debug("{} Initialized", MOD_ID);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
