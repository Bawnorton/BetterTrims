package com.bawnorton.effect;

import com.bawnorton.material.CustomArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;


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

    public static final ArmorTrimEffect PRISMARINE = of(CustomArmorTrimMaterials.PRISMARINE);
    public static final ArmorTrimEffect ECHO_SHARD = of(CustomArmorTrimMaterials.ECHO);

    private static ArmorTrimEffect of(RegistryKey<ArmorTrimMaterial> material) {
        return new ArmorTrimEffect(material);
    }
}
