package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.vanilla.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public record ChangeItemDamageAbility(CountBasedValue amount) implements TrimEntityAbility {
	public static final MapCodec<ChangeItemDamageAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("amount").forGetter(ChangeItemDamageAbility::amount)
	).apply(instance, ChangeItemDamageAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		Map<EquipmentSlot, ItemStack> equippedStacks = new HashMap<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = wearer.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				equippedStacks.put(slot, stack);
			}
		}
		ItemStack stack = targetSlot != null ? wearer.getItemBySlot(targetSlot) : ItemStack.EMPTY;
		int amount = (int) this.amount.calculate(items.size());
		if (stack.isEmpty()) {
			for (Map.Entry<EquipmentSlot, ItemStack> stackEntry : equippedStacks.entrySet()) {
				damage(level, wearer, stackEntry.getValue(), stackEntry.getKey(), items.onBreak(), amount);
			}
		} else {
			damage(level, wearer, stack, targetSlot, items.onBreak(), amount);
		}
	}

	private void damage(ServerLevel level, LivingEntity wearer, ItemStack stack, EquipmentSlot slot, BiConsumer<Item, EquipmentSlot> onBreak, int amount) {
		if (stack.has(DataComponents.MAX_DAMAGE) && stack.has(DataComponents.DAMAGE)) {
			ServerPlayer owner = wearer instanceof ServerPlayer ? (ServerPlayer) wearer : null;
			stack.hurtAndBreak(amount, level, owner, item -> onBreak.accept(item, slot));
		}
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<ChangeItemDamageAbility> {
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ChangeItemDamageAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.change_item_damage.damages", Styler::negative)
					.cycle(builder -> element.amount().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.translate("bettertrims.tooltip.ability.change_item_damage.points", Styler::negative)
					.spaced()
					.build();
		}
	}
}
