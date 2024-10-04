package com.bawnorton.bettertrims.platform;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;

public final class Platform {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}

//?} elif neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

public final class Platform {
    public static boolean isModLoaded(String modId) {
        ModList modList = ModList.get();
        if(modList != null) {
            return modList.isLoaded(modId);
        }
        LoadingModList loadingModList = LoadingModList.get();
        if(loadingModList != null) {
            return loadingModList.getModFileById(modId) != null;
        }
        return false;
    }
}
*///?}