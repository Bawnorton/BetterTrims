package com.bawnorton.bettertrims.data.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class AdditionalItemTags {
    //? if >=1.21 {
    /*public static final TagKey<Item> ADAMANTITE_INGOTS = register("ingots/adamantite");
    public static final TagKey<Item> AQUARIUM_INGOTS = register("ingots/aquarium");
    public static final TagKey<Item> BANGLUM_INGOTS = register("ingots/banglum");
    public static final TagKey<Item> BRONZE_INGOTS = register("ingots/bronze");
    public static final TagKey<Item> CARMOT_INGOTS = register("ingots/carmot");
    public static final TagKey<Item> CELESTIUM_INGOTS = register("ingots/celestium");
    public static final TagKey<Item> DURASTEEL_INGOTS = register("ingots/durasteel");
    public static final TagKey<Item> HALLOWED_INGOTS = register("ingots/hallowed");
    public static final TagKey<Item> KYBER_INGOTS = register("ingots/kyber");
    public static final TagKey<Item> MANGANESE_INGOTS = register("ingots/manganese");
    public static final TagKey<Item> METALLURGIUM_INGOTS = register("ingots/metallurgium");
    public static final TagKey<Item> MIDAS_GOLD_INGOTS = register("ingots/midas_gold");
    public static final TagKey<Item> MYTHRIL_INGOTS = register("ingots/mythril");
    public static final TagKey<Item> ORICHALCUM_INGOTS = register("ingots/orichalcum");
    public static final TagKey<Item> OSMIUM_INGOTS = register("ingots/osmium");
    public static final TagKey<Item> PALLADIUM_INGOTS = register("ingots/palladium");
    public static final TagKey<Item> PLATINUM_INGOTS = register("ingots/platinum");
    public static final TagKey<Item> PROMETHEUM_INGOTS = register("ingots/prometheum");
    public static final TagKey<Item> QUADRILLUM_INGOTS = register("ingots/quadrillum");
    public static final TagKey<Item> RUNITE_INGOTS = register("ingots/runite");
    public static final TagKey<Item> SILVER_INGOTS = register("ingots/silver");
    public static final TagKey<Item> STEEL_INGOTS = register("ingots/steel");
    public static final TagKey<Item> STORMYX_INGOTS = register("ingots/stormyx");
    public static final TagKey<Item> TIN_INGOTS = register("ingots/tin");

    public static final TagKey<Item> STORAGE_BLOCKS_ADAMANTITE = register("storage_blocks/adamantite");
    public static final TagKey<Item> STORAGE_BLOCKS_AQUARIUM = register("storage_blocks/aquarium");
    public static final TagKey<Item> STORAGE_BLOCKS_BANGLUM = register("storage_blocks/banglum");
    public static final TagKey<Item> STORAGE_BLOCKS_BRONZE = register("storage_blocks/bronze");
    public static final TagKey<Item> STORAGE_BLOCKS_CARMOT = register("storage_blocks/carmot");
    public static final TagKey<Item> STORAGE_BLOCKS_CELESTIUM = register("storage_blocks/celestium");
    public static final TagKey<Item> STORAGE_BLOCKS_DURASTEEL = register("storage_blocks/durasteel");
    public static final TagKey<Item> STORAGE_BLOCKS_HALLOWED = register("storage_blocks/hallowed");
    public static final TagKey<Item> STORAGE_BLOCKS_KYBER = register("storage_blocks/kyber");
    public static final TagKey<Item> STORAGE_BLOCKS_MANGANESE = register("storage_blocks/manganese");
    public static final TagKey<Item> STORAGE_BLOCKS_METALLURGIUM = register("storage_blocks/metallurgium");
    public static final TagKey<Item> STORAGE_BLOCKS_MIDAS_GOLD = register("storage_blocks/midas_gold");
    public static final TagKey<Item> STORAGE_BLOCKS_MYTHRIL = register("storage_blocks/mythril");
    public static final TagKey<Item> STORAGE_BLOCKS_ORICHALCUM = register("storage_blocks/orichalcum");
    public static final TagKey<Item> STORAGE_BLOCKS_OSMIUM = register("storage_blocks/osmium");
    public static final TagKey<Item> STORAGE_BLOCKS_PALLADIUM = register("storage_blocks/palladium");
    public static final TagKey<Item> STORAGE_BLOCKS_PLATINUM = register("storage_blocks/platinum");
    public static final TagKey<Item> STORAGE_BLOCKS_PROMETHEUM = register("storage_blocks/prometheum");
    public static final TagKey<Item> STORAGE_BLOCKS_QUADRILLUM = register("storage_blocks/quadrillum");
    public static final TagKey<Item> STORAGE_BLOCKS_RUNITE = register("storage_blocks/runite");
    public static final TagKey<Item> STORAGE_BLOCKS_SILVER = register("storage_blocks/silver");
    public static final TagKey<Item> STORAGE_BLOCKS_STEEL = register("storage_blocks/steel");
    public static final TagKey<Item> STORAGE_BLOCKS_STORMYX = register("storage_blocks/stormyx");
    public static final TagKey<Item> STORAGE_BLOCKS_TIN = register("storage_blocks/tin");

    public static final TagKey<Item> STARRITE = register("starrite");
    public static final TagKey<Item> STAR_PLATINUM = register("star_platinum");
    public static final TagKey<Item> UNOBTAINIUM = register("unobtainium");

    public static final TagKey<Item> STORAGE_BLOCKS_STARRITE = register("storage_blocks/starrite");
    public static final TagKey<Item> STORAGE_BLOCKS_STAR_PLATINUM = register("storage_blocks/star_platinum");
    public static final TagKey<Item> STORAGE_BLOCKS_UNOBTAINIUM = register("storage_blocks/unobtainium");

    private static TagKey<Item> register(String path) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", path));
    }

    *///?} else {
    public static final TagKey<Item> ADAMANTITE_INGOTS = register("adamantite_ingots");
    public static final TagKey<Item> AQUARIUM_INGOTS = register("aquarium_ingots");
    public static final TagKey<Item> BANGLUM_INGOTS = register("banglum_ingots");
    public static final TagKey<Item> BRONZE_INGOTS = register("bronze_ingots");
    public static final TagKey<Item> CARMOT_INGOTS = register("carmot_ingots");
    public static final TagKey<Item> CELESTIUM_INGOTS = register("celestium_ingots");
    public static final TagKey<Item> DURASTEEL_INGOTS = register("durasteel_ingots");
    public static final TagKey<Item> HALLOWED_INGOTS = register("hallowed_ingots");
    public static final TagKey<Item> KYBER_INGOTS = register("kyber_ingots");
    public static final TagKey<Item> MANGANESE_INGOTS = register("manganese_ingots");
    public static final TagKey<Item> METALLURGIUM_INGOTS = register("metallurgium_ingots");
    public static final TagKey<Item> MIDAS_GOLD_INGOTS = register("midas_gold_ingots");
    public static final TagKey<Item> MYTHRIL_INGOTS = register("mythril_ingots");
    public static final TagKey<Item> ORICHALCUM_INGOTS = register("orichalcum_ingots");
    public static final TagKey<Item> OSMIUM_INGOTS = register("osmium_ingots");
    public static final TagKey<Item> PALLADIUM_INGOTS = register("palladium_ingots");
    public static final TagKey<Item> PLATINUM_INGOTS = register("platinum_ingots");
    public static final TagKey<Item> PROMETHEUM_INGOTS = register("prometheum_ingots");
    public static final TagKey<Item> QUADRILLUM_INGOTS = register("quadrillum_ingots");
    public static final TagKey<Item> RUNITE_INGOTS = register("runite_ingots");
    public static final TagKey<Item> SILVER_INGOTS = register("silver_ingots");
    public static final TagKey<Item> STEEL_INGOTS = register("steel_ingots");
    public static final TagKey<Item> STORMYX_INGOTS = register("stormyx_ingots");
    public static final TagKey<Item> TIN_INGOTS = register("tin_ingots");

    public static final TagKey<Item> STORAGE_BLOCKS_ADAMANTITE = register("adamantite_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_AQUARIUM = register("aquarium_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_BANGLUM = register("banglum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_BRONZE = register("bronze_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_CARMOT = register("carmot_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_CELESTIUM = register("celestium_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_DURASTEEL = register("durasteel_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_HALLOWED = register("hallowed_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_KYBER = register("kyber_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_MANGANESE = register("manganese_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_METALLURGIUM = register("metallurgium_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_MIDAS_GOLD = register("midas_gold_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_MYTHRIL = register("mythril_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_ORICHALCUM = register("orichalcum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_OSMIUM = register("osmium_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_PALLADIUM = register("palladium_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_PLATINUM = register("platinum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_PROMETHEUM = register("prometheum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_QUADRILLUM = register("quadrillum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_RUNITE = register("runite_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_SILVER = register("silver_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_STEEL = register("steel_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_STORMYX = register("stormyx_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_TIN = register("tin_blocks");

    public static final TagKey<Item> STARRITE = register("starrite");
    public static final TagKey<Item> STAR_PLATINUM = register("star_platinum");
    public static final TagKey<Item> UNOBTAINIUM = register("unobtainium");

    public static final TagKey<Item> STORAGE_BLOCKS_STARRITE = register("starrite_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_STAR_PLATINUM = register("star_platinum_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_UNOBTAINIUM = register("unobtainium_blocks");

    private static TagKey<Item> register(String path) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier("c", path));
    }
    //?}
}
