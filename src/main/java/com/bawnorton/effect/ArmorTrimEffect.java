package com.bawnorton.effect;

import com.bawnorton.BetterTrims;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ArmorTrimEffect {
    private final RegistryKey<ArmorTrimMaterial> material;

    protected ArmorTrimEffect(RegistryKey<ArmorTrimMaterial> matieral) {
        this.material = matieral;
    }

    private Identifier getTrimMaterial(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return null;
        NbtElement trimElement = nbt.get("Trim");
        if(!(trimElement instanceof NbtCompound nbtCompound)) return null;
        return new Identifier(nbtCompound.getString("material"));
    }

    public boolean apply(ItemStack stack) {
        return TrimEffectLookup.get(material).equals(getTrimMaterial(stack));
    }
}
