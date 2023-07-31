package com.bawnorton.bettertrims.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class StackedTrimsCompat {
    public static List<Identifier> getTrimMaterials(ItemStack stack) {
        if(!Compat.isStackedTrimsLoaded()) throw new IllegalStateException("Stacked Trims is not loaded");
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return null;
        NbtElement trimElement = nbt.get("Trim");
        if (!(trimElement instanceof NbtList list)) return null;
        List<Identifier> materials = new ArrayList<>();
        for (NbtElement element : list) {
            if (!(element instanceof NbtCompound nbtCompound)) continue;
            materials.add(new Identifier(nbtCompound.getString("material")));
        }
        return materials;
    }
}
