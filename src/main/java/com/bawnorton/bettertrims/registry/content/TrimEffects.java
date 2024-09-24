package com.bawnorton.bettertrims.registry.content;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.tag.TrimMaterialTags;
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

    public static final AdamantiteTrimEffect ADAMANTITE = register("adamantite", TrimMaterialTags.ADAMANTITE, AdamantiteTrimEffect::new);
    public static final AquariumTrimEffect AQUARIUM = register("aquarium", TrimMaterialTags.AQUARIUM, AquariumTrimEffect::new);
    public static final BanglumTrimEffect BANGLUM = register("banglum", TrimMaterialTags.BANGLUM, BanglumTrimEffect::new);
    public static final BronzeTrimEffect BRONZE = register("bronze", TrimMaterialTags.BRONZE, BronzeTrimEffect::new);
    public static final CarmotTrimEffect CARMOT = register("carmot", TrimMaterialTags.CARMOT, CarmotTrimEffect::new);
    public static final CelestiumTrimEffect CELESTIUM = register("celestium", TrimMaterialTags.CELESTIUM, CelestiumTrimEffect::new);
    public static final DurasteelTrimEffect DURASTEEL = register("durasteel", TrimMaterialTags.DURASTEEL, DurasteelTrimEffect::new);
    public static final HallowedTrimEffect HALLOWED = register("hallowed", TrimMaterialTags.HALLOWED, HallowedTrimEffect::new);
    public static final KyberTrimEffect KYBER = register("kyber", TrimMaterialTags.KYBER, KyberTrimEffect::new);
    public static final ManganeseTrimEffect MANGANESE = register("manganese", TrimMaterialTags.MANGANESE, ManganeseTrimEffect::new);
    public static final MetallurgiumTrimEffect METALLURGIUM = register("metallurgium", TrimMaterialTags.METALLURGIUM, MetallurgiumTrimEffect::new);
    public static final MidasGoldTrimEffect MIDAS_GOLD = register("midas_gold", TrimMaterialTags.MIDAS_GOLD, MidasGoldTrimEffect::new);
    public static final MythrilTrimEffect MYTHRIL = register("mythril", TrimMaterialTags.MYTHRIL, MythrilTrimEffect::new);
    public static final OrichalcumTrimEffect ORICHALCUM = register("orichalcum", TrimMaterialTags.ORICHALCUM, OrichalcumTrimEffect::new);
    public static final OsmiumTrimEffect OSMIUM = register("osmium", TrimMaterialTags.OSMIUM, OsmiumTrimEffect::new);
    public static final PalladiumTrimEffect PALLADIUM = register("palladium", TrimMaterialTags.PALLADIUM, PalladiumTrimEffect::new);
    public static final PlatinumTrimEffect PLATINUM = register("platinum", TrimMaterialTags.PLATINUM, PlatinumTrimEffect::new);
    public static final PrometheumTrimEffect PROMETHEUM = register("prometheum", TrimMaterialTags.PROMETHEUM, PrometheumTrimEffect::new);
    public static final QuadrillumTrimEffect QUADRILLUM = register("quadrillum", TrimMaterialTags.QUADRILLUM, QuadrillumTrimEffect::new);
    public static final RuniteTrimEffect RUNITE = register("runite", TrimMaterialTags.RUNITE, RuniteTrimEffect::new);
    public static final SilverTrimEffect SILVER = register("silver", TrimMaterialTags.SILVER, SilverTrimEffect::new);
    public static final StarriteTrimEffect STARRITE = register("starrite", TrimMaterialTags.STARRITE, StarriteTrimEffect::new);
    public static final StarPlatinumTrimEffect STAR_PLATINUM = register("star_platinum", TrimMaterialTags.STAR_PLATINUM, StarPlatinumTrimEffect::new);
    public static final SteelTrimEffect STEEL = register("steel", TrimMaterialTags.STEEL, SteelTrimEffect::new);
    public static final StormyxTrimEffect STORMYX = register("stormyx", TrimMaterialTags.STORMYX, StormyxTrimEffect::new);
    public static final TinTrimEffect TIN = register("tin", TrimMaterialTags.TIN, TinTrimEffect::new);
    public static final UnobtainiumTrimEffect UNOBTAINIUM = register("unobtainium", TrimMaterialTags.UNOBTAINIUM, UnobtainiumTrimEffect::new);

    private static <T extends TrimEffect> T register(String id, TagKey<Item> materials, TrimEffect.Factory<T> factory) {
        return Registry.register(TrimRegistries.TRIM_EFFECTS, BetterTrims.id(id), factory.create(materials));
    }

    public static void init() {
        //no-op
    }
}
