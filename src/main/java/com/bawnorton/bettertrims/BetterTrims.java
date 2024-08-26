package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.networking.Networking;
import com.bawnorton.bettertrims.registry.content.TrimLootTables;
import com.bawnorton.bettertrims.util.Probabilities;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterTrims {
    public static final String MOD_ID = "bettertrims";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Probabilities PROBABILITIES = new Probabilities(42L);

    public static void init() {
        LOGGER.debug("{} Initialized", MOD_ID);

        Networking.init();
        TrimLootTables.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static String sid(String path) {
        return id(path).toString();
    }
}
