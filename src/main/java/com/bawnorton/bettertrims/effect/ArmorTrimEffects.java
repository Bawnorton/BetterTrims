package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.util.RegexIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

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

    // modded materials
    public static final ArmorTrimEffect PLATINUM = of(new RegexIdentifier(".*", "platinum"));
    public static final ArmorTrimEffect SILVER = of(new RegexIdentifier(".*", "silver"));

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