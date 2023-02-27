package com.bawnorton.effect;

import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Map;

public abstract class TrimEffectLookup {
    private static final Map<RegistryKey<ArmorTrimMaterial>, Identifier> TRIM_MATERIALS = Map.ofEntries(
            Map.entry(ArmorTrimMaterials.AMETHYST, new Identifier("amethyst")),
            Map.entry(ArmorTrimMaterials.COPPER, new Identifier("copper")),
            Map.entry(ArmorTrimMaterials.DIAMOND, new Identifier("diamond")),
            Map.entry(ArmorTrimMaterials.EMERALD, new Identifier("emerald")),
            Map.entry(ArmorTrimMaterials.GOLD, new Identifier("gold")),
            Map.entry(ArmorTrimMaterials.IRON, new Identifier("iron")),
            Map.entry(ArmorTrimMaterials.LAPIS, new Identifier("lapis")),
            Map.entry(ArmorTrimMaterials.NETHERITE, new Identifier("netherite")),
            Map.entry(ArmorTrimMaterials.QUARTZ, new Identifier("quartz")),
            Map.entry(ArmorTrimMaterials.REDSTONE, new Identifier("redstone"))
    );

    public static Identifier get(RegistryKey<ArmorTrimMaterial> material) {
        return TRIM_MATERIALS.get(material);
    }
}
