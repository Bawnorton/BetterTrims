package com.bawnorton.bettertrims.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import java.util.concurrent.CompletableFuture;

//? if >=1.21 {
/*import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
*///?} else {
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
//?}

public final class TrimMaterialTagProvider extends FabricTagProvider.ItemTagProvider {
    public TrimMaterialTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //? if fabric {
        getOrCreateTagBuilder(TrimMaterialTags.QUARTZ)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.QUARTZ_GEMS);
                *///?} else {
                .forceAddTag(ConventionalItemTags.QUARTZ);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.IRON)
                .forceAddTag(ConventionalItemTags.IRON_INGOTS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_IRON);
                *///?} else {
                .add(Items.IRON_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.NETHERITE)
                .forceAddTag(ConventionalItemTags.NETHERITE_INGOTS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_NETHERITE);
                *///?} else {
                .add(Items.NETHERITE_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.REDSTONE)
                .forceAddTag(ConventionalItemTags.REDSTONE_DUSTS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE);
                *///?} else {
                .add(Items.REDSTONE_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.COPPER)
                .forceAddTag(ConventionalItemTags.COPPER_INGOTS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_COPPER);
                *///?} else {
                .add(Items.COPPER_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.GOLD)
                .forceAddTag(ConventionalItemTags.GOLD_INGOTS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_GOLD);
                *///?} else {
                .add(Items.GOLD_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.EMERALD)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.EMERALD_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_EMERALD);
                *///?} else {
                .forceAddTag(ConventionalItemTags.EMERALDS)
                .add(Items.EMERALD_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.DIAMOND)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.DIAMOND_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_DIAMOND);
                *///?} else {
                .forceAddTag(ConventionalItemTags.DIAMONDS)
                .add(Items.DIAMOND_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.LAPIS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.LAPIS_GEMS)
                .forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_LAPIS);
                *///?} else {
                .forceAddTag(ConventionalItemTags.LAPIS)
                .add(Items.LAPIS_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.AMETHYST)
                .add(Items.AMETHYST_BLOCK)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.AMETHYST_GEMS);
                *///?} else {
                .add(Items.AMETHYST_SHARD);
                //?}

        getOrCreateTagBuilder(TrimMaterialTags.COAL)
                .forceAddTag(ItemTags.COALS)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_COAL);
                *///?} else {
                .add(Items.COAL_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.DRAGONS_BREATH)
                .add(Items.DRAGON_BREATH);
        getOrCreateTagBuilder(TrimMaterialTags.CHORUS_FRUIT)
                .add(Items.CHORUS_FRUIT);
        getOrCreateTagBuilder(TrimMaterialTags.ECHO_SHARD)
                .add(Items.ECHO_SHARD);
        getOrCreateTagBuilder(TrimMaterialTags.ENDER_PEARL)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.ENDER_PEARLS);
                *///?} else {
                .add(Items.ENDER_PEARL);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.FIRE_CHARGE)
                .add(Items.FIRE_CHARGE);
        getOrCreateTagBuilder(TrimMaterialTags.GLOWSTONE)
                .add(Items.GLOWSTONE)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.GLOWSTONE_DUSTS);
                *///?} else {
                .add(Items.GLOWSTONE_DUST);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.LEATHER)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.LEATHERS);
                *///?} else {
                .add(Items.LEATHER);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.NETHER_BRICK)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.NETHER_BRICKS);
                *///?} else {
                .add(Items.NETHER_BRICKS);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.PRISMARINE)
                .add(Items.PRISMARINE_SHARD)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.PRISMARINE_GEMS);
                *///?} else {
                .add(Items.PRISMARINE_CRYSTALS);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.RABBIT)
                .add(Items.RABBIT_FOOT)
                .add(Items.RABBIT_HIDE);
        getOrCreateTagBuilder(TrimMaterialTags.SLIME)
                .add(Items.SLIME_BALL)
                //? if >=1.21 {
                /*.forceAddTag(ConventionalItemTags.STORAGE_BLOCKS_SLIME);
                *///?} else {
                .add(Items.SLIME_BLOCK);
                //?}
        getOrCreateTagBuilder(TrimMaterialTags.ENCHANTED_GOLDEN_APPLE)
                .add(Items.ENCHANTED_GOLDEN_APPLE);
        //?}
    }
}