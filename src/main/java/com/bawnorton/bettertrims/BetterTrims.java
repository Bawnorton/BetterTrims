package com.bawnorton.bettertrims;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterTrims {
    public static final String MOD_ID = "bettertrims";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.debug("{} Initialized", MOD_ID);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
