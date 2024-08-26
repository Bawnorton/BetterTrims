package com.bawnorton.bettertrims.registry.content;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class TrimMaterialTags implements Iterable<TagKey<Item>> {
    private static final Set<TagKey<Item>> TAGS = new HashSet<>();

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

    private static TagKey<Item> register(String material) {
        TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, BetterTrims.id("trim_materials/%s".formatted(material)));
        TAGS.add(tagKey);
        return tagKey;
    }

    @NotNull
    @Override
    public Iterator<TagKey<Item>> iterator() {
        return TAGS.iterator();
    }
}
