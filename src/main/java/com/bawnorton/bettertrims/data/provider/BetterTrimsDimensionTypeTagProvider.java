package com.bawnorton.bettertrims.data.provider;

import com.bawnorton.bettertrims.data.BetterTrimsDimensionTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

public class BetterTrimsDimensionTypeTagProvider extends FabricTagProvider<DimensionType> {
	public BetterTrimsDimensionTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DIMENSION_TYPE, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		builder(BetterTrimsDimensionTypeTags.HAS_SUN)
				.add(BuiltinDimensionTypes.OVERWORLD);
		builder(BetterTrimsDimensionTypeTags.HAS_MOON)
				.add(BuiltinDimensionTypes.OVERWORLD);
	}

	//? if 1.21.1 {
	/*private FabricTagBuilder builder(TagKey<DimensionType> type) {
		return getOrCreateTagBuilder(type);
	}
	*///?}
}
