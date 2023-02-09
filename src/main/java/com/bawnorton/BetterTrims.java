package com.bawnorton;

import com.bawnorton.config.Config;
import com.bawnorton.config.ConfigManager;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrims implements ModInitializer {
    public static final String MOD_ID = "bettertrims";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Config CONFIG = Config.getInstance();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Better Trims");
        ConfigManager.loadConfig();
    }
}