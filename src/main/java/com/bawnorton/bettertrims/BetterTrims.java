package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.networking.Networking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
//?} else {
//?}

public final class BetterTrims {
	public static final String MOD_ID = "bettertrims";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceLocation DEFAULT = BetterTrims.rl("default");
	public static final ResourceLocation TRIM_EFFECTS = BetterTrims.rl("trim_effects");

	public static void init() {
		Networking.init();
		registerResourcePacks();
		LOGGER.debug("{} Initialized", MOD_ID);

	}

	private static void registerResourcePacks() {
		//? if fabric {
		ResourceManagerHelper.registerBuiltinResourcePack(
				TRIM_EFFECTS,
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
				Component.translatable("bettertrims.resourcepack.effects"),
				ResourcePackActivationType.NORMAL
		);

		ResourceManagerHelper.registerBuiltinResourcePack(
				DEFAULT,
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
				Component.translatable("bettertrims.resourcepack.default"),
				ResourcePackActivationType.DEFAULT_ENABLED
		);
		//?} else {

		//?}
	}

	public static ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
