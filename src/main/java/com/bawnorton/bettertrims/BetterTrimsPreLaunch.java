package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.config.ConfigManager;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class BetterTrimsPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        BetterTrims.LOGGER.debug("PreLaunching Better Trims");
        ConfigManager.loadConfigs();
    }

}
