package com.bawnorton.bettertrims.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;

public final class TrimMaterialTagProvider extends FabricTagProvider.ItemTagProvider {
    public TrimMaterialTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(TrimMaterialTags.QUARTZ)
                .forceAddTag(ConventionalItemTags.QUARTZ_GEMS);
        getOrCreateTagBuilder(TrimMaterialTags.IRON)
                .forceAddTag(ConventionalItemTags.IRON_INGOTS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_IRON);
        getOrCreateTagBuilder(TrimMaterialTags.NETHERITE)
                .forceAddTag(ConventionalItemTags.NETHERITE_INGOTS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_NETHERITE);
        getOrCreateTagBuilder(TrimMaterialTags.REDSTONE)
                .forceAddTag(ConventionalItemTags.REDSTONE_DUSTS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE);
        getOrCreateTagBuilder(TrimMaterialTags.COPPER)
                .forceAddTag(ConventionalItemTags.COPPER_INGOTS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_COPPER);
        getOrCreateTagBuilder(TrimMaterialTags.GOLD)
                .forceAddTag(ConventionalItemTags.GOLD_INGOTS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_GOLD);
        getOrCreateTagBuilder(TrimMaterialTags.EMERALD)
                .forceAddTag(ConventionalItemTags.EMERALD_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_EMERALD);
        getOrCreateTagBuilder(TrimMaterialTags.DIAMOND)
                .forceAddTag(ConventionalItemTags.DIAMOND_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_DIAMOND);
        getOrCreateTagBuilder(TrimMaterialTags.LAPIS)
                .forceAddTag(ConventionalItemTags.LAPIS_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_LAPIS);
        getOrCreateTagBuilder(TrimMaterialTags.AMETHYST)
                .forceAddTag(ConventionalItemTags.AMETHYST_GEMS);
    }
}
