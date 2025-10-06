package com.bawnorton.bettertrims.version;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface VRegistry {
	static <T> Registry<T> get(ClientLevel level, ResourceKey<? extends Registry<T>> key) {
		//? if >=1.21.8 {
		return level.registryAccess().lookupOrThrow(key);
		 //?} else {
		/*return level.registryAccess().registryOrThrow(key);
		*///?}
	}
}
