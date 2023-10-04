package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.compat.Compat;
import com.bawnorton.bettertrims.compat.StackedTrimsCompat;
import com.bawnorton.bettertrims.util.RegexPath;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ArmorTrimEffect {
    private final RegexPath material;
    private final Text tooltip;

    public ArmorTrimEffect(RegexPath matieral, Text tooltip) {
        this.material = matieral;
        this.tooltip = tooltip;
    }

    private Identifier getTrimMaterial(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return null;

        NbtElement trimElement = nbt.get("Trim");
        if (!(trimElement instanceof NbtCompound nbtCompound)) return null;

        return new Identifier(nbtCompound.getString("material"));
    }

    public boolean appliesTo(ItemStack stack) {
        if (Compat.isStackedTrimsLoaded()) {
            List<Identifier> materials = StackedTrimsCompat.getTrimMaterials(stack);
            if (materials == null) return false;

            return material.matchesAny(materials);
        }
        return material.matches(getTrimMaterial(stack));
    }

    public boolean appliesTo(Iterable<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (appliesTo(stack)) return true;
        }
        return false;
    }

    public void apply(Iterable<ItemStack> armour, Effect effect) {
        for (ItemStack stack : armour) {
            if (appliesTo(stack)) effect.applyEffect();
        }
    }

    public String getMaterial() {
        return material.toString();
    }

    public Text getTooltip() {
        return tooltip;
    }

    @FunctionalInterface
    public interface Effect {
        void applyEffect();
    }
}
