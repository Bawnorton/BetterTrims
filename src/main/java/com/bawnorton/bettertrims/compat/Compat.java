package com.bawnorton.bettertrims.compat;

import net.fabricmc.loader.api.FabricLoader;

public abstract class Compat {
    public static boolean isStackedTrimsLoaded() {
        return FabricLoader.getInstance().isModLoaded("stacked_trims") || FabricLoader.getInstance().isModLoaded("stackable_trims");
    }
}
