package com.bawnorton;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTrimsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(BetterTrims.MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Better Trims Client");
	}
}