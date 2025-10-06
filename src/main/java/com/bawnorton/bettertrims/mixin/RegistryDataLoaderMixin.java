package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.google.common.collect.ImmutableList;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@MixinEnvironment
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
		ImmutableList.Builder<RegistryDataLoader.RegistryData<?>> synchronizedBuilder = ImmutableList.builder();
		synchronizedBuilder.addAll(SYNCHRONIZED_REGISTRIES);
		synchronizedBuilder.add(new RegistryDataLoader.RegistryData<>(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperty.DIRECT_CODEC, false));
		SYNCHRONIZED_REGISTRIES = synchronizedBuilder.build();

		ImmutableList.Builder<RegistryDataLoader.RegistryData<?>> worldgenBuilder = ImmutableList.builder();
		worldgenBuilder.addAll(WORLDGEN_REGISTRIES);
		worldgenBuilder.add(new RegistryDataLoader.RegistryData<>(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperty.DIRECT_CODEC, false));
		WORLDGEN_REGISTRIES = worldgenBuilder.build();
	}
}
