package com.bawnorton.material;

import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

// not implemented yet
public class CustomArmorTrimMaterials {
    public static RegistryKey<ArmorTrimMaterial> PRISMARINE = RegistryKey.of(RegistryKeys.TRIM_MATERIAL, new Identifier("prismarine"));
    public static RegistryKey<ArmorTrimMaterial> ECHO = RegistryKey.of(RegistryKeys.TRIM_MATERIAL, new Identifier("echo"));
}
