package com.bawnorton.bettertrims.registry.content;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.*;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;

public final class TrimEffects {
    public static final AmethystTrimEffect AMETHYST = register("amethyst", TrimMaterialTags.AMETHYST, AmethystTrimEffect::new);
    public static final CopperTrimEffect COPPER = register("copper", TrimMaterialTags.COPPER, CopperTrimEffect::new);
    public static final DiamondTrimEffect DIAMOND = register("diamond", TrimMaterialTags.DIAMOND, DiamondTrimEffect::new);
    public static final EmeraldTrimEffect EMERALD = register("emerald", TrimMaterialTags.EMERALD, EmeraldTrimEffect::new);
    public static final GoldTrimEffect GOLD = register("gold", TrimMaterialTags.GOLD, GoldTrimEffect::new);
    public static final IronTrimEffect IRON = register("iron", TrimMaterialTags.IRON, IronTrimEffect::new);
    public static final LapisTrimEffect LAPIS = register("lapis", TrimMaterialTags.LAPIS, LapisTrimEffect::new);
    public static final NetheriteTrimEffect NETHERITE = register("netherite", TrimMaterialTags.NETHERITE, NetheriteTrimEffect::new);
    public static final QuartzTrimEffect QUARTZ = register("quartz", TrimMaterialTags.QUARTZ, QuartzTrimEffect::new);
    public static final RedstoneTrimEffect REDSTONE = register("redstone", TrimMaterialTags.REDSTONE, RedstoneTrimEffect::new);

    public static final ChorusFruitTrimEffect CHORUS_FRUIT = register("chorus_fruit", TrimMaterialTags.CHORUS_FRUIT, ChorusFruitTrimEffect::new);
    public static final CoalTrimEffect COAL = register("coal", TrimMaterialTags.COAL, CoalTrimEffect::new);
    public static final DragonsBreathTrimEffect DRAGONS_BREATH = register("dragons_breath", TrimMaterialTags.DRAGONS_BREATH, DragonsBreathTrimEffect::new);
    public static final EchoShardTrimEffect ECHO_SHARD = register("echo_shard", TrimMaterialTags.ECHO_SHARD, EchoShardTrimEffect::new);
    public static final EnchantedGoldenAppleTrimEffect ENCHANTED_GOLDEN_APPLE = register("enchanted_golden_apple", TrimMaterialTags.ENCHANTED_GOLDEN_APPLE, EnchantedGoldenAppleTrimEffect::new);
    public static final EnderPearlTrimEffect ENDER_PEARL = register("ender_pearl", TrimMaterialTags.ENDER_PEARL, EnderPearlTrimEffect::new);
    public static final FireChargeTrimEffect FIRE_CHARGE = register("fire_charge", TrimMaterialTags.FIRE_CHARGE, FireChargeTrimEffect::new);
    public static final GlowstoneTrimEffect GLOWSTONE = register("glowstone", TrimMaterialTags.GLOWSTONE, GlowstoneTrimEffect::new);
    public static final LeatherTrimEffect LEATHER = register("leather", TrimMaterialTags.LEATHER, LeatherTrimEffect::new);
    public static final NetherBrickTrimEffect NETHER_BRICK = register("nether_brick", TrimMaterialTags.NETHER_BRICK, NetherBrickTrimEffect::new);
    public static final PrismarineTrimEffect PRISMARINE = register("prismarine", TrimMaterialTags.PRISMARINE, PrismarineTrimEffect::new);
    public static final RabbitTrimEffect RABBIT = register("rabbit", TrimMaterialTags.RABBIT, RabbitTrimEffect::new);
    public static final SlimeTrimEffect SLIME = register("slime", TrimMaterialTags.SLIME, SlimeTrimEffect::new);

    private static <T extends TrimEffect> T register(String id, TagKey<Item> materials, TrimEffect.Factory<T> factory) {
        return Registry.register(
                TrimRegistries.TRIM_EFFECTS,
                BetterTrims.id(id),
                factory.create(materials)
        );
    }

    public static void init() {
        //no-op
    }
}
