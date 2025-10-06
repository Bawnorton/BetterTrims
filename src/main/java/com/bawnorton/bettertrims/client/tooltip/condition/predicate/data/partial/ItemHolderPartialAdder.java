package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.ItemPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;

public abstract class ItemHolderPartialAdder<T extends /*$ item_data_predicate >>*/ DataComponentPredicate > implements PartialAdder<T> {
	protected void addItemHolderToBuilder(ClientLevel level, CollectionPredicate<ItemStack, ItemPredicate> items, String prefix, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (items != null) {
			addCollectionToBuilder(
					level,
					items,
					prefix,
					(ignoredLevel, itemPredicate, predicateState, collectionBuilder) -> ItemPredicateTooltip.addToBuilder(
							level,
							itemPredicate,
							predicateState.withPrefixSpace(false),
							collectionBuilder
					),
					state,
					builder
			);
		} else {
			builder.space().translate(key("%s.any").formatted(builder), Styler::condition);
		}
	}
}
