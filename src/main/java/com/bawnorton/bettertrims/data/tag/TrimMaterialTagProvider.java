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
import net.minecraft.registry.tag.TagKey;
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

        getOrCreateTagBuilder(TrimMaterialTags.ADAMANTITE)
				.forceAddTag(AdditionalItemTags.ADAMANTITE_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_ADAMANTITE);
        getOrCreateTagBuilder(TrimMaterialTags.AQUARIUM)
				.forceAddTag(AdditionalItemTags.AQUARIUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_AQUARIUM);
        getOrCreateTagBuilder(TrimMaterialTags.BANGLUM)
				.forceAddTag(AdditionalItemTags.BANGLUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_BANGLUM);
        getOrCreateTagBuilder(TrimMaterialTags.BRONZE)
				.forceAddTag(AdditionalItemTags.BRONZE_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_BRONZE);
        getOrCreateTagBuilder(TrimMaterialTags.CARMOT)
				.forceAddTag(AdditionalItemTags.CARMOT_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_CARMOT);
        getOrCreateTagBuilder(TrimMaterialTags.CELESTIUM)
				.forceAddTag(AdditionalItemTags.CELESTIUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_CELESTIUM);
        getOrCreateTagBuilder(TrimMaterialTags.DURASTEEL)
				.forceAddTag(AdditionalItemTags.DURASTEEL_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_DURASTEEL);
        getOrCreateTagBuilder(TrimMaterialTags.HALLOWED)
				.forceAddTag(AdditionalItemTags.HALLOWED_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_HALLOWED);
        getOrCreateTagBuilder(TrimMaterialTags.KYBER)
				.forceAddTag(AdditionalItemTags.KYBER_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_KYBER);
        getOrCreateTagBuilder(TrimMaterialTags.MANGANESE)
				.forceAddTag(AdditionalItemTags.MANGANESE_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_MANGANESE);
        getOrCreateTagBuilder(TrimMaterialTags.METALLURGIUM)
				.forceAddTag(AdditionalItemTags.METALLURGIUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_METALLURGIUM);
        getOrCreateTagBuilder(TrimMaterialTags.MIDAS_GOLD)
				.forceAddTag(AdditionalItemTags.MIDAS_GOLD_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_MIDAS_GOLD);
        getOrCreateTagBuilder(TrimMaterialTags.MYTHRIL)
				.forceAddTag(AdditionalItemTags.MYTHRIL_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_MYTHRIL);
        getOrCreateTagBuilder(TrimMaterialTags.ORICHALCUM)
				.forceAddTag(AdditionalItemTags.ORICHALCUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_ORICHALCUM);
        getOrCreateTagBuilder(TrimMaterialTags.OSMIUM)
				.forceAddTag(AdditionalItemTags.OSMIUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_OSMIUM);
        getOrCreateTagBuilder(TrimMaterialTags.PALLADIUM)
				.forceAddTag(AdditionalItemTags.PALLADIUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_PALLADIUM);
        getOrCreateTagBuilder(TrimMaterialTags.PLATINUM)
				.forceAddTag(AdditionalItemTags.PLATINUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_PLATINUM);
        getOrCreateTagBuilder(TrimMaterialTags.PROMETHEUM)
				.forceAddTag(AdditionalItemTags.PROMETHEUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_PROMETHEUM);
        getOrCreateTagBuilder(TrimMaterialTags.QUADRILLUM)
				.forceAddTag(AdditionalItemTags.QUADRILLUM_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_QUADRILLUM);
        getOrCreateTagBuilder(TrimMaterialTags.RUNITE)
				.forceAddTag(AdditionalItemTags.RUNITE_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_RUNITE);
        getOrCreateTagBuilder(TrimMaterialTags.SILVER)
				.forceAddTag(AdditionalItemTags.SILVER_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_SILVER);
        getOrCreateTagBuilder(TrimMaterialTags.STARRITE)
				.forceAddTag(AdditionalItemTags.STARRITE)
                .forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_STARRITE);
        getOrCreateTagBuilder(TrimMaterialTags.STAR_PLATINUM)
				.forceAddTag(AdditionalItemTags.STAR_PLATINUM)
                .forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_STAR_PLATINUM);
        getOrCreateTagBuilder(TrimMaterialTags.STEEL)
				.forceAddTag(AdditionalItemTags.STEEL_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_STEEL);
        getOrCreateTagBuilder(TrimMaterialTags.STORMYX)
				.forceAddTag(AdditionalItemTags.STORMYX_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_STORMYX);
        getOrCreateTagBuilder(TrimMaterialTags.TIN)
				.forceAddTag(AdditionalItemTags.TIN_INGOTS)
				.forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_TIN);
        getOrCreateTagBuilder(TrimMaterialTags.UNOBTAINIUM)
				.forceAddTag(AdditionalItemTags.UNOBTAINIUM)
                .forceAddTag(AdditionalItemTags.STORAGE_BLOCKS_UNOBTAINIUM);
        //?}
    }
}