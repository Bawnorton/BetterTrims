package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.config.ConfigManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class ArmorTrimEffects {
    private static final List<ArmorTrimEffect> EFFECTS = new ArrayList<>();

    public static final ArmorTrimEffect QUARTZ = of(TrimMaterial.of(ArmorTrimMaterials.QUARTZ), () -> ConfigManager.getConfig().enableQuartz);
    public static final ArmorTrimEffect IRON = of(TrimMaterial.of(ArmorTrimMaterials.IRON), () -> ConfigManager.getConfig().enableIron);
    public static final ArmorTrimEffect NETHERITE = of(TrimMaterial.of(ArmorTrimMaterials.NETHERITE), () -> ConfigManager.getConfig().enableNetherite);
    public static final ArmorTrimEffect REDSTONE = of(TrimMaterial.of(ArmorTrimMaterials.REDSTONE), () -> ConfigManager.getConfig().enableRedstone);
    public static final ArmorTrimEffect COPPER = of(TrimMaterial.of(ArmorTrimMaterials.COPPER), () -> ConfigManager.getConfig().enableCopper);
    public static final ArmorTrimEffect GOLD = of(TrimMaterial.of(ArmorTrimMaterials.GOLD), () -> ConfigManager.getConfig().enableGold);
    public static final ArmorTrimEffect EMERALD = of(TrimMaterial.of(ArmorTrimMaterials.EMERALD), () -> ConfigManager.getConfig().enableEmerald);
    public static final ArmorTrimEffect DIAMOND = of(TrimMaterial.of(ArmorTrimMaterials.DIAMOND), () -> ConfigManager.getConfig().enableDiamond);
    public static final ArmorTrimEffect LAPIS = of(TrimMaterial.of(ArmorTrimMaterials.LAPIS), () -> ConfigManager.getConfig().enableLapis);
    public static final ArmorTrimEffect AMETHYST = of(TrimMaterial.of(ArmorTrimMaterials.AMETHYST), () -> ConfigManager.getConfig().enableAmethyst);
    public static final ArmorTrimEffect COAL = of(TrimMaterial.of(Items.COAL), () -> ConfigManager.getConfig().enableCoal);
    public static final ArmorTrimEffect DRAGONS_BREATH = of(TrimMaterial.of(Items.DRAGON_BREATH)
                                                                        .or(TrimMaterial.of("dragon")), () -> ConfigManager.getConfig().enableDragonsBreath);
    public static final ArmorTrimEffect CHORUS_FRUIT = of(TrimMaterial.of(Items.CHORUS_FRUIT), () -> ConfigManager.getConfig().enableChorusFruit);
    public static final ArmorTrimEffect ECHO_SHARD = of(TrimMaterial.of(Items.ECHO_SHARD)
                                                                    .or(TrimMaterial.of("echo")), () -> ConfigManager.getConfig().enableEchoShard);
    public static final ArmorTrimEffect ENDER_PEARL = of(TrimMaterial.of(Items.ENDER_PEARL)
                                                                     .or(TrimMaterial.of("ender")), () -> ConfigManager.getConfig().enableEnderPearl);
    public static final ArmorTrimEffect FIRE_CHARGE = of(TrimMaterial.of(Items.FIRE_CHARGE)
                                                                     .or(TrimMaterial.of("flame")), () -> ConfigManager.getConfig().enableFireCharge);
    public static final ArmorTrimEffect GLOWSTONE_DUST = of(TrimMaterial.of(Items.GLOWSTONE_DUST)
                                                                        .or(TrimMaterial.of("glowstone")), () -> ConfigManager.getConfig().enableGlowstoneDust);
    public static final ArmorTrimEffect LEATHER = of(TrimMaterial.of(Items.LEATHER), () -> ConfigManager.getConfig().enableLeather);
    public static final ArmorTrimEffect NETHER_BRICK = of(TrimMaterial.of(Items.NETHER_BRICK)
                                                                      .or(TrimMaterial.of("netherbrick")), () -> ConfigManager.getConfig().enableNetherBrick);
    public static final ArmorTrimEffect PRISMARINE_SHARD = of(TrimMaterial.of(Items.PRISMARINE_SHARD)
                                                                          .or(TrimMaterial.of("prismarine")), () -> ConfigManager.getConfig().enablePrismarineShard);
    public static final ArmorTrimEffect RABBIT_HIDE = of(TrimMaterial.of(Items.RABBIT_HIDE)
                                                                     .or(TrimMaterial.of("rabbit")), () -> ConfigManager.getConfig().enableRabbitHide);
    public static final ArmorTrimEffect SLIME_BALL = of(TrimMaterial.of(Items.SLIME_BALL)
                                                                    .or(TrimMaterial.of("slime")), () -> ConfigManager.getConfig().enableSlimeBall);
    public static final ArmorTrimEffect ENCHANTED_GOLDEN_APPLE = of(TrimMaterial.of(Items.ENCHANTED_GOLDEN_APPLE)
                                                                                .or(TrimMaterial.of("rainbow")), () -> ConfigManager.getConfig().enableEnchantedGoldenApple);
    public static final ArmorTrimEffect PLATINUM = of(TrimMaterial.of("platinum"), () -> ConfigManager.getConfig().enablePlatinum);
    public static final ArmorTrimEffect SILVER = of(TrimMaterial.of("silver"), () -> ConfigManager.getConfig().enableSilver);

    public static ArmorTrimEffect of(TrimMaterial material, BooleanSupplier enabled) {
        ArmorTrimEffect effect = new ArmorTrimEffect(material, enabled, material.getTooltip());
        EFFECTS.add(effect);
        return effect;
    }

    public static void forEachAppliedEffect(ItemStack stack, Consumer<ArmorTrimEffect> effectConsumer) {
        for (ArmorTrimEffect effect : EFFECTS) {
            if (effect.appliesTo(stack)) {
                effectConsumer.accept(effect);
            }
        }
    }
}
