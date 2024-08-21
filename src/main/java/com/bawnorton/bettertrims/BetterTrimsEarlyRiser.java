package com.bawnorton.bettertrims;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public final class BetterTrimsEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String operation = remapper.mapClassName("intermediary", "net.minecraft.class_1322.class_1323");
        ClassTinkerers.enumBuilder(operation, String.class, Integer.class)
                .addEnum("ADD_PERCENTAGE", "add_percentage", 3)
                .build();
    }
}
