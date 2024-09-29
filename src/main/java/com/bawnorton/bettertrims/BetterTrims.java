package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributeApplicator;
import com.bawnorton.bettertrims.networking.Networking;
import com.bawnorton.bettertrims.data.loot.TrimLootTables;
import com.bawnorton.bettertrims.util.Probabilities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
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

        //? if <1.21 {
        /*ServerWorldEvents.LOAD.register((server, world) -> TrimEntityAttributeApplicator.registryManager = world.getRegistryManager());
        *///?}
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static String sid(String path) {
        return id(path).toString();
    }
}
