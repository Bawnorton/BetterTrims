package com.bawnorton.effect;

import com.bawnorton.compat.StackedTrimsCompat;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class ArmorTrimEffect {
    private final RegistryKey<ArmorTrimMaterial> material;

    protected ArmorTrimEffect(RegistryKey<ArmorTrimMaterial> matieral) {
        this.material = matieral;
    }

    private Identifier getTrimMaterial(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return null;
        NbtElement trimElement = nbt.get("Trim");
        if (!(trimElement instanceof NbtCompound nbtCompound)) return null;
        return new Identifier(nbtCompound.getString("material"));
    }

    public boolean appliesTo(ItemStack stack) {
        if (StackedTrimsCompat.isLoaded()) {
            List<Identifier> materials = StackedTrimsCompat.getTrimMaterials(stack);
            if (materials == null) return false;
            return materials.contains(TrimEffectLookup.get(material));
        }
        return TrimEffectLookup.get(material).equals(getTrimMaterial(stack));
    }

    public void apply(Iterable<ItemStack> armour, Effect effect) {
        for (ItemStack stack : armour) {
            if (appliesTo(stack)) effect.applyEffect(stack);
        }
    }

    @FunctionalInterface
    public interface Effect {
        void applyEffect(ItemStack stack);
    }
}
