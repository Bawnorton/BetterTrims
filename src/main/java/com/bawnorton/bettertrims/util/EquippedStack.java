package com.bawnorton.bettertrims.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;

public record EquippedStack(ItemStack itemStack, EquipmentSlot slot) {
    public static EquippedStack of(ItemStack itemStack, EquipmentSlot slot) {
        return new EquippedStack(itemStack, slot);
    }

    public boolean correctSlot() {
        return itemStack.getItem() instanceof Equipment equipment && equipment.getSlotType() == slot;
    }
}
