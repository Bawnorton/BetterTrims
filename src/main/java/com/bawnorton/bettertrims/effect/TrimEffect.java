package com.bawnorton.bettertrims.effect;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.tag.TagKey;

public final class TrimEffect {
    private final TagKey<Item> materials;

    private TrimEffect(TagKey<Item> materials) {
        this.materials = materials;
    }

    public static TrimEffect of(TagKey<Item> materials) {
        return new TrimEffect(materials);
    }

    public boolean matches(ItemStack stack) {
        ComponentMap map = stack.getComponents();
        ArmorTrim trim = map.get(DataComponentTypes.TRIM);
        if(trim == null) return false;

        ArmorTrimMaterial material = trim.getMaterial().value();
        return material.ingredient().isIn(materials);
    }

    public boolean matches(Iterable<ItemStack> stacks) {
        return matchCount(stacks) > 0;
    }

    public boolean matches(LivingEntity entity) {
        return matchCount(entity) > 0;
    }

    public int matchCount(Iterable<ItemStack> stacks) {
        int matches = 0;
        for(ItemStack stack : stacks) {
            if (matches(stack)) {
                matches++;
            }
        }
        return matches;
    }

    public int matchCount(LivingEntity entity) {
        return matchCount(entity.getAllArmorItems());
    }
}
