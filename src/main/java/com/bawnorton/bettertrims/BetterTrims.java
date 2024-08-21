package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.potion.TrimStatusEffects;
import com.bawnorton.bettertrims.networking.Networking;
import com.bawnorton.bettertrims.util.Probabilities;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterTrims {
    public static final String MOD_ID = "bettertrims";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Probabilities PROBABILITIES = new Probabilities();

    public static void init() {
        LOGGER.debug("{} Initialized", MOD_ID);
        Networking.init();
        TrimEntityAttributes.init();
        TrimStatusEffects.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
