package com.bawnorton.bettertrims.effect;

import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;


public abstract class ArmorTrimEffects {
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
    public static final ArmorTrimEffect PLATINUM = of(new Identifier("illagerinvasion", "platinum"));

    private static ArmorTrimEffect of(RegistryKey<ArmorTrimMaterial> material) {
        return of(material.getValue());
    }

    private static ArmorTrimEffect of(Identifier material) {
        return new ArmorTrimEffect(material);
    }
}
