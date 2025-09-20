package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import java.util.HashMap;
import java.util.Map;

public final class BetterTrimsClient {
    private static final Map<Holder<TrimPattern>, HolderSet<Item>> PATTERN_SOURCES = new HashMap<>();

    public static void init() {
        ClientNetworking.init();
        BetterTrims.LOGGER.debug("{} Client Initialized", BetterTrims.MOD_ID);
    }

    public static void setPatternSources(Map<Holder<TrimPattern>, HolderSet<Item>> patternSources) {
        PATTERN_SOURCES.clear();
        PATTERN_SOURCES.putAll(patternSources);
    }

    public static Map<Holder<TrimPattern>, HolderSet<Item>> getPatternSources() {
        return PATTERN_SOURCES;
    }
}
