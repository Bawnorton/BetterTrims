package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.compat.Compat;
import com.bawnorton.bettertrims.compat.StackedTrimsCompat;
import com.bawnorton.bettertrims.util.EquippedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ArmorTrimEffect {
    private final TrimMaterial material;
    private final BooleanSupplier enabled;
    private final Text tooltip;

    public ArmorTrimEffect(TrimMaterial matieral, BooleanSupplier enabled, Text tooltip) {
        this.material = matieral;
        this.enabled = enabled;
        this.tooltip = tooltip;
    }

    private String getTrimMaterial(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return null;

        NbtElement trimElement = nbt.get("Trim");
        if (!(trimElement instanceof NbtCompound nbtCompound)) return null;

        return new Identifier(nbtCompound.getString("material")).getPath();
    }

    public boolean appliesTo(EquippedStack stack) {
        if (!stack.correctSlot()) return false;

        return appliesTo(stack.itemStack());
    }

    public boolean appliesTo(ItemStack stack) {
        if (!enabled.getAsBoolean()) return false;

        if (Compat.isStackedTrimsLoaded()) {
            List<String> materials = StackedTrimsCompat.getTrimMaterials(stack);
            if (materials == null) return false;

            return material.appliesTo(materials);
        }
        String material = getTrimMaterial(stack);
        return material != null && this.material.appliesTo(material);
    }

    public boolean appliesTo(Iterable<EquippedStack> stacks) {
        for (EquippedStack stack : stacks) {
            if (appliesTo(stack)) return true;
        }
        return false;
    }

    public void apply(Iterable<EquippedStack> armour, Effect effect) {
        for (EquippedStack stack : armour) {
            if (appliesTo(stack)) effect.applyEffect();
        }
    }

    public Text getTooltip() {
        return tooltip;
    }

    @FunctionalInterface
    public interface Effect {
        void applyEffect();
    }
}
