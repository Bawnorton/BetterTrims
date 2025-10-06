package com.bawnorton.bettertrims.data.provider;
//? if fabric {

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
//?} else {

/*import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.BetterTrimsDimensionTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BetterTrimsDimensionTypeTagProvider extends TagsProvider<DimensionType> {
	public BetterTrimsDimensionTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.DIMENSION_TYPE, lookupProvider, BetterTrims.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		tag(BetterTrimsDimensionTypeTags.HAS_SUN)
				.add(BuiltinDimensionTypes.OVERWORLD);
		tag(BetterTrimsDimensionTypeTags.HAS_MOON)
				.add(BuiltinDimensionTypes.OVERWORLD);
	}
}
*///?}