package com.bawnorton;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrims implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("bettertrims");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}