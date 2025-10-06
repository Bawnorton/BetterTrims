package com.bawnorton.bettertrims.data.provider;

//? if fabric {
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
//?} else {

/*import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.BetterTrimsEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

//? if >=1.21.8 {
import net.minecraft.data.tags.TagAppender;
//?} else {
/^import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
^///?}

public class BetterTrimsEntityTypeTagProvider extends TagsProvider<EntityType<?>> {
	//? if <1.21.8 {
	/^public BetterTrimsEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, Registries.ENTITY_TYPE, lookupProvider, BetterTrims.MOD_ID, existingFileHelper);
	}
	^///?} else {
	public BetterTrimsEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Registries.ENTITY_TYPE, lookupProvider, BetterTrims.MOD_ID);
	}
	//?}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		tag(BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES)
				.addTag(EntityTypeTags.ARROWS)
				.add(EntityType.TRIDENT.builtInRegistryHolder().key());
	}

	//? if >=1.21.8 {
	private TagAppender<ResourceKey<EntityType<?>>, EntityType<?>> tag(TagKey<EntityType<?>> type) {
		return TagAppender.forBuilder(getOrCreateRawBuilder(type));
	}
	//?}
}

*///?}