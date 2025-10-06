package com.bawnorton.bettertrims.data.provider;

import com.bawnorton.bettertrims.data.BetterTrimsEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class BetterTrimsEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public BetterTrimsEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		valueLookupBuilder(BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES)
				.forceAddTag(EntityTypeTags.ARROWS)
				.add(EntityType.TRIDENT);
	}

	//? if 1.21.1 {
	/*private FabricTagBuilder valueLookupBuilder(TagKey<EntityType<?>> type) {
		return getOrCreateTagBuilder(type);
	}
	*///?}
}
