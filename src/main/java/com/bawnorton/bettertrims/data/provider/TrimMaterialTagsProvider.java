package com.bawnorton.bettertrims.data.provider;

//? if fabric {
import com.bawnorton.bettertrims.data.TrimMaterialTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.concurrent.CompletableFuture;

public class TrimMaterialTagsProvider extends FabricTagProvider<TrimMaterial> {
	public TrimMaterialTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.TRIM_MATERIAL, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		builder(TrimMaterialTags.QUARTZ).setReplace(false).add(TrimMaterials.QUARTZ);
		builder(TrimMaterialTags.IRON).setReplace(false).add(TrimMaterials.IRON);
		builder(TrimMaterialTags.NETHERITE).setReplace(false).add(TrimMaterials.NETHERITE);
		builder(TrimMaterialTags.REDSTONE).setReplace(false).add(TrimMaterials.REDSTONE);
		builder(TrimMaterialTags.COPPER).setReplace(false).add(TrimMaterials.COPPER);
		builder(TrimMaterialTags.GOLD).setReplace(false).add(TrimMaterials.GOLD);
		builder(TrimMaterialTags.EMERALD).setReplace(false).add(TrimMaterials.EMERALD);
		builder(TrimMaterialTags.DIAMOND).setReplace(false).add(TrimMaterials.DIAMOND);
		builder(TrimMaterialTags.LAPIS).setReplace(false).add(TrimMaterials.LAPIS);
		builder(TrimMaterialTags.AMETHYST).setReplace(false).add(TrimMaterials.AMETHYST);
		builder(TrimMaterialTags.SILVER).setReplace(false);
		//? if >=1.21.8 {
		builder(TrimMaterialTags.RESIN).setReplace(false).add(TrimMaterials.RESIN);
		 //?}
	}

	//? if <1.21.8 {
	/*private FabricTagBuilder builder(TagKey<TrimMaterial> type) {
		return getOrCreateTagBuilder(type);
	}
	*///?}
}
//?} else {
/*import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.TrimMaterialTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

//? if >=1.21.8 {
import net.minecraft.data.tags.TagAppender;
//?} else {
/^import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
^///?}

public class TrimMaterialTagsProvider extends TagsProvider<TrimMaterial> {
	//? if >=1.21.8 {
	public TrimMaterialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Registries.TRIM_MATERIAL, lookupProvider, BetterTrims.MOD_ID);
	}
	//?} else {
	/^public TrimMaterialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.TRIM_MATERIAL, lookupProvider, BetterTrims.MOD_ID, existingFileHelper);
	}
	^///?}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		tag(TrimMaterialTags.QUARTZ).replace(false).add(TrimMaterials.QUARTZ);
		tag(TrimMaterialTags.IRON).replace(false).add(TrimMaterials.IRON);
		tag(TrimMaterialTags.NETHERITE).replace(false).add(TrimMaterials.NETHERITE);
		tag(TrimMaterialTags.REDSTONE).replace(false).add(TrimMaterials.REDSTONE);
		tag(TrimMaterialTags.COPPER).replace(false).add(TrimMaterials.COPPER);
		tag(TrimMaterialTags.GOLD).replace(false).add(TrimMaterials.GOLD);
		tag(TrimMaterialTags.EMERALD).replace(false).add(TrimMaterials.EMERALD);
		tag(TrimMaterialTags.DIAMOND).replace(false).add(TrimMaterials.DIAMOND);
		tag(TrimMaterialTags.LAPIS).replace(false).add(TrimMaterials.LAPIS);
		tag(TrimMaterialTags.AMETHYST).replace(false).add(TrimMaterials.AMETHYST);
		tag(TrimMaterialTags.SILVER).replace(false);
		//? if >=1.21.8 {
		tag(TrimMaterialTags.RESIN).replace(false).add(TrimMaterials.RESIN);
		 //?}
	}

	//? if >=1.21.8 {
	private TagAppender<ResourceKey<TrimMaterial>, TrimMaterial> tag(TagKey<TrimMaterial> tag) {
		return TagAppender.forBuilder(getOrCreateRawBuilder(tag));
	}
	//?}
}
*///?}