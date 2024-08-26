package com.bawnorton.bettertrims.data.tag;

import com.bawnorton.bettertrims.registry.content.TrimMaterialTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import java.util.concurrent.CompletableFuture;

public final class TrimMaterialTagProvider extends FabricTagProvider.ItemTagProvider {
    public TrimMaterialTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //? if fabric {
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

        getOrCreateTagBuilder(TrimMaterialTags.COAL)
                .forceAddTag(ItemTags.COALS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_COAL);
        getOrCreateTagBuilder(TrimMaterialTags.DRAGONS_BREATH)
                .add(Items.DRAGON_BREATH);
        getOrCreateTagBuilder(TrimMaterialTags.CHORUS_FRUIT)
                .add(Items.CHORUS_FRUIT);
        getOrCreateTagBuilder(TrimMaterialTags.ECHO_SHARD)
                .add(Items.ECHO_SHARD);
        getOrCreateTagBuilder(TrimMaterialTags.ENDER_PEARL)
                .forceAddTag(ConventionalItemTags.ENDER_PEARLS);
        getOrCreateTagBuilder(TrimMaterialTags.FIRE_CHARGE)
                .add(Items.FIRE_CHARGE);
        getOrCreateTagBuilder(TrimMaterialTags.GLOWSTONE)
                .forceAddTag(ConventionalItemTags.GLOWSTONE_DUSTS);
        getOrCreateTagBuilder(TrimMaterialTags.LEATHER)
                .forceAddTag(ConventionalItemTags.LEATHERS);
        getOrCreateTagBuilder(TrimMaterialTags.NETHER_BRICK)
                .forceAddTag(ConventionalItemTags.NETHER_BRICKS);
        getOrCreateTagBuilder(TrimMaterialTags.PRISMARINE)
                .forceAddTag(ConventionalItemTags.PRISMARINE_GEMS)
                .add(Items.PRISMARINE_SHARD);
        getOrCreateTagBuilder(TrimMaterialTags.RABBIT)
                .add(Items.RABBIT_FOOT)
                .add(Items.RABBIT_HIDE);
        getOrCreateTagBuilder(TrimMaterialTags.SLIME)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_SLIME)
                .add(Items.SLIME_BALL);
        getOrCreateTagBuilder(TrimMaterialTags.ENCHANTED_GOLDEN_APPLE)
                .add(Items.ENCHANTED_GOLDEN_APPLE);
        //?}
    }
}
