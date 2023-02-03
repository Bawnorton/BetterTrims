package com.bawnorton.effect;

import com.bawnorton.material.CustomArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Map;

public abstract class TrimEffectLookup {
    private static final Map<RegistryKey<ArmorTrimMaterial>, Identifier> TRIM_MATERIALS = Map.ofEntries(
            Map.entry(ArmorTrimMaterials.AMETHYST, new Identifier("minecraft", "amethyst")),
            Map.entry(ArmorTrimMaterials.COPPER, new Identifier("minecraft", "copper")),
            Map.entry(ArmorTrimMaterials.DIAMOND, new Identifier("minecraft", "diamond")),
            Map.entry(ArmorTrimMaterials.EMERALD, new Identifier("minecraft", "emerald")),
            Map.entry(ArmorTrimMaterials.GOLD, new Identifier("minecraft", "gold")),
            Map.entry(ArmorTrimMaterials.IRON, new Identifier("minecraft", "iron")),
            Map.entry(ArmorTrimMaterials.LAPIS, new Identifier("minecraft", "lapis")),
            Map.entry(ArmorTrimMaterials.NETHERITE, new Identifier("minecraft", "netherite")),
            Map.entry(ArmorTrimMaterials.QUARTZ, new Identifier("minecraft", "quartz")),
            Map.entry(ArmorTrimMaterials.REDSTONE, new Identifier("minecraft", "redstone")),
            Map.entry(CustomArmorTrimMaterials.PRISMARINE, new Identifier("minecraft", "prismarine")),
            Map.entry(CustomArmorTrimMaterials.ECHO, new Identifier("minecraft", "echo"))
    );

    public static Identifier get(RegistryKey<ArmorTrimMaterial> material) {
        return TRIM_MATERIALS.get(material);
    }
}
