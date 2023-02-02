package com.bawnorton.effect;

import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.RegistryKey;

public class ArmorTrimEffect {
    private final RegistryKey<ArmorTrimMaterial> material;

    public ArmorTrimEffect(RegistryKey<ArmorTrimMaterial> matieral) {
        this.material = matieral;
    }

    public RegistryKey<ArmorTrimMaterial> getMaterial() {
        return material;
    }
}
