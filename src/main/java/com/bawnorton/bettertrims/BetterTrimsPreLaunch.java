package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class BetterTrimsPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        BetterTrims.LOGGER.debug("PreLaunching Better Trims");
        ConfigManager.loadConfig();
        MixinExtrasBootstrap.init();
    }
}
