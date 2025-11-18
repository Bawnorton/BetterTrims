package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.bawnorton.bettertrims.util.AppendableForwardingList;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(RegistryDataLoader.class)
abstract class RegistryDataLoaderMixin {
	@Shadow
	@Final
	@Mutable
	public static List<RegistryDataLoader.RegistryData<?>> SYNCHRONIZED_REGISTRIES;

	@Shadow
	@Final
	@Mutable
	public static List<RegistryDataLoader.RegistryData<?>> WORLDGEN_REGISTRIES;

	static {
		SYNCHRONIZED_REGISTRIES = new AppendableForwardingList<>(SYNCHRONIZED_REGISTRIES);
		SYNCHRONIZED_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperty.DIRECT_CODEC, false));

		WORLDGEN_REGISTRIES = new AppendableForwardingList<>(WORLDGEN_REGISTRIES);
		WORLDGEN_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperty.DIRECT_CODEC, false));
	}
}