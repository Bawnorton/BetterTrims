package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.util.RegexIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public abstract class ArmorTrimEffects {
    private static final List<ArmorTrimEffect> EFFECTS = new ArrayList<>();

    public static final ArmorTrimEffect QUARTZ = of(ArmorTrimMaterials.QUARTZ);
    public static final ArmorTrimEffect IRON = of(ArmorTrimMaterials.IRON);
    public static final ArmorTrimEffect NETHERITE = of(ArmorTrimMaterials.NETHERITE);
    public static final ArmorTrimEffect REDSTONE = of(ArmorTrimMaterials.REDSTONE);
    public static final ArmorTrimEffect COPPER = of(ArmorTrimMaterials.COPPER);
    public static final ArmorTrimEffect GOLD = of(ArmorTrimMaterials.GOLD);
    public static final ArmorTrimEffect EMERALD = of(ArmorTrimMaterials.EMERALD);
    public static final ArmorTrimEffect DIAMOND = of(ArmorTrimMaterials.DIAMOND);
    public static final ArmorTrimEffect LAPIS = of(ArmorTrimMaterials.LAPIS);
    public static final ArmorTrimEffect AMETHYST = of(ArmorTrimMaterials.AMETHYST);

    // added vanilla materials
    public static final ArmorTrimEffect COAL = of(Items.COAL);
    public static final ArmorTrimEffect DRAGONS_BREATH = of(Items.DRAGON_BREATH);
    public static final ArmorTrimEffect CHORUS_FRUIT = of(Items.CHORUS_FRUIT);
    public static final ArmorTrimEffect ECHO_SHARD = of(Items.ECHO_SHARD);
    public static final ArmorTrimEffect ENDER_PEARL = of(Items.ENDER_PEARL);
    public static final ArmorTrimEffect FIRE_CHARGE = of(Items.FIRE_CHARGE);
    public static final ArmorTrimEffect GLOWSTONE_DUST = of(Items.GLOWSTONE_DUST);
    public static final ArmorTrimEffect LEATHER = of(Items.LEATHER);
    public static final ArmorTrimEffect NETHER_BRICK = of(Items.NETHER_BRICK);
    public static final ArmorTrimEffect PRISMARINE_SHARD = of(Items.PRISMARINE_SHARD);
    public static final ArmorTrimEffect RABBIT_HIDE = of(Items.RABBIT_HIDE);
    public static final ArmorTrimEffect SLIME_BALL = of(Items.SLIME_BALL);

    // modded materials
    public static final ArmorTrimEffect PLATINUM = of(new RegexIdentifier(".*", "platinum"));
    public static final ArmorTrimEffect SILVER = of(new RegexIdentifier(".*", "silver"));



    private static ArmorTrimEffect of(Item item) {
        return of(new RegexIdentifier(".*", Registries.ITEM.getId(item).getPath()));
    }

    private static ArmorTrimEffect of(RegistryKey<ArmorTrimMaterial> material) {
        return of(new RegexIdentifier(".*", material.getValue().getPath()));
    }

    private static ArmorTrimEffect of(RegexIdentifier material) {
        ArmorTrimEffect effect = new ArmorTrimEffect(material, getTooltip(material.path()));
        EFFECTS.add(effect);
        return effect;
    }

    private static Text getTooltip(String path) {
        return Text.translatable("effect.bettertrims." + path + ".tooltip");
    }

    public static void forEachEffect(ItemStack stack, Consumer<ArmorTrimEffect> effectConsumer) {
        for(ArmorTrimEffect effect : EFFECTS) {
            if(effect.appliesTo(stack)) {
                effectConsumer.accept(effect);
            }
        }
    }
}
