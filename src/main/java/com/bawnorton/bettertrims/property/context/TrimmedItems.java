package com.bawnorton.bettertrims.property.context;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public record TrimmedItems(Map<EquipmentSlot, ItemStack> stacks, LivingEntity owner,
                           BiConsumer<Item, EquipmentSlot> onBreak) implements Iterable<Map.Entry<EquipmentSlot, ItemStack>> {
	public static TrimmedItems of(Map<EquipmentSlot, ItemStack> stacks, LivingEntity owner) {
		return new TrimmedItems(stacks, owner, owner::onEquippedItemBroken);
	}

	public int size() {
		return stacks.size();
	}

	public ItemStack get(EquipmentSlot slot) {
		if (slot == null) return ItemStack.EMPTY;

		return stacks.get(slot);
	}

	public List<ItemStack> getItems() {
		return stacks.values().stream().toList();
	}

	@Override
	public @NotNull Iterator<Map.Entry<EquipmentSlot, ItemStack>> iterator() {
		return stacks.entrySet().iterator();
	}
}
