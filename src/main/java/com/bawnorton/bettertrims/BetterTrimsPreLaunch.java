package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.util.List;

public class BetterTrimsPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        BetterTrims.LOGGER.debug("PreLaunching Better Trims");
        ConfigManager.loadConfigs();
    }

}
