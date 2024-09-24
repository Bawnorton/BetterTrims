package com.bawnorton.bettertrims.data.tag;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class TrimMaterialTags {
    public static final TagKey<Item> AMETHYST = register("amethyst");
    public static final TagKey<Item> COPPER = register("copper");
    public static final TagKey<Item> DIAMOND = register("diamond");
    public static final TagKey<Item> EMERALD = register("emerald");
    public static final TagKey<Item> GOLD = register("gold");
    public static final TagKey<Item> IRON = register("iron");
    public static final TagKey<Item> LAPIS = register("lapis");
    public static final TagKey<Item> NETHERITE = register("netherite");
    public static final TagKey<Item> QUARTZ = register("quartz");
    public static final TagKey<Item> REDSTONE = register("redstone");
    
    public static final TagKey<Item> COAL = register("coal");
    public static final TagKey<Item> DRAGONS_BREATH = register("dragons_breath");
    public static final TagKey<Item> CHORUS_FRUIT = register("chorus_fruit");
    public static final TagKey<Item> ECHO_SHARD = register("echo_shard");
    public static final TagKey<Item> ENDER_PEARL = register("ender_pearl");
    public static final TagKey<Item> FIRE_CHARGE = register("fire_charge");
    public static final TagKey<Item> GLOWSTONE = register("glowstone");
    public static final TagKey<Item> LEATHER = register("leather");
    public static final TagKey<Item> NETHER_BRICK = register("nether_brick");
    public static final TagKey<Item> PRISMARINE = register("prismarine");
    public static final TagKey<Item> RABBIT = register("rabbit");
    public static final TagKey<Item> SLIME = register("slime");
    public static final TagKey<Item> ENCHANTED_GOLDEN_APPLE = register("enchanted_golden_apple");

    public static final TagKey<Item> ADAMANTITE = register("adamantite");
    public static final TagKey<Item> AQUARIUM = register("aquarium");
    public static final TagKey<Item> BANGLUM = register("banglum");
    public static final TagKey<Item> BRONZE = register("bronze");
    public static final TagKey<Item> CARMOT = register("carmot");
    public static final TagKey<Item> CELESTIUM = register("celestium");
    public static final TagKey<Item> DURASTEEL = register("durasteel");
    public static final TagKey<Item> HALLOWED = register("hallowed");
    public static final TagKey<Item> KYBER = register("kyber");
    public static final TagKey<Item> MANGANESE = register("manganese");
    public static final TagKey<Item> METALLURGIUM = register("metallurgium");
    public static final TagKey<Item> MIDAS_GOLD = register("midas_gold");
    public static final TagKey<Item> MYTHRIL = register("mythril");
    public static final TagKey<Item> ORICHALCUM = register("orichalcum");
    public static final TagKey<Item> OSMIUM = register("osmium");
    public static final TagKey<Item> PALLADIUM = register("palladium");
    public static final TagKey<Item> PLATINUM = register("platinum");
    public static final TagKey<Item> PROMETHEUM = register("prometheum");
    public static final TagKey<Item> QUADRILLUM = register("quadrillum");
    public static final TagKey<Item> RUNITE = register("runite");
    public static final TagKey<Item> SILVER = register("silver");
    public static final TagKey<Item> STARRITE = register("starrite");
    public static final TagKey<Item> STAR_PLATINUM = register("star_platinum");
    public static final TagKey<Item> STEEL = register("steel");
    public static final TagKey<Item> STORMYX = register("stormyx");
    public static final TagKey<Item> TIN = register("tin");
    public static final TagKey<Item> UNOBTAINIUM = register("unobtainium");

    private static TagKey<Item> register(String material) {
        return TagKey.of(RegistryKeys.ITEM, BetterTrims.id("trim_materials/%s".formatted(material)));
    }
}
