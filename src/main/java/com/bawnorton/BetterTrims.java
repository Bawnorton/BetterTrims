package com.bawnorton;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class BetterTrims implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("bettertrims");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Better Trims");
		MixinExtrasBootstrap.init();
	}


}