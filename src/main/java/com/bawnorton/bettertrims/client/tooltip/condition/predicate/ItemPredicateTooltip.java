package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.ExactDataComponentPredicateTooltipAdders;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.PartialDataComponentPredicateTooltipAdders;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

//? if >=1.21.8 {
import net.minecraft.advancements.critereon.DataComponentMatchers;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.DataComponentMatchersTooltip;
//?} else {
/*import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.component.DataComponentPredicate;
*///?}

import static com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip.addMinMaxToBuilder;

public interface ItemPredicateTooltip {
	static void addToBuilder(ClientLevel level, ItemPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<HolderSet<Item>> items = predicate.items();
		if (items.isPresent()) {
			addItemsToBuilder(level, items.orElseThrow(), state, builder);
			state = state.withUseWith(true);
		}

		//? if >=1.21.8 {
		DataComponentMatchers components = predicate.components();
		if (!components.isEmpty()) {
			addDataComponentMatchersToBuilder(level, components, state, builder);
		}
		//?} else {
		/*DataComponentPredicate components = predicate.components();
		if (!components.asPatch().isEmpty()) {
			addDataComponentMatchersToBuilder(level, components, state, builder);
		}

		Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates = predicate.subPredicates();
		if (!subPredicates.isEmpty()) {
			addSubPredicatesToBuilder(level, subPredicates, state, builder);
		}
		*///?}

		MinMaxBounds.Ints count = predicate.count();
		if (!count.isAny()) {
			addCountToBuilder(level, count, state, builder);
		}
	}


	static String key(String key) {
		return PredicateTooltip.key("item.%s".formatted(key));
	}

	static void addItemsToBuilder(ClientLevel level, HolderSet<Item> items, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PredicateTooltip.addRegisteredElementsToBuilder(
				level,
				key("matches"),
				Registries.ITEM,
				items,
				//? if >=1.21.8 {
				item -> item.getName(),
				//?} else {
				/*item -> item.getName(item.getDefaultInstance()),
				 *///?}
				state,
				builder
		);
	}

	//? if >=1.21.8 {
	static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (state.doPrefixSpace()) {
			builder.space();
		}
		builder.translate("bettertrims.tooltip.condition.match_tool.has.%s".formatted(state.key()), Styler::condition);
		DataComponentMatchersTooltip.addToBuilder(level, components, state, builder);
	}
	//?} else {
	/*static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (state.doPrefixSpace()) {
			builder.space();
		}
		builder.translate(key("data"), Styler::condition);

		Set<Map.Entry<DataComponentType<?>, Optional<?>>> entries = predicate.asPatch().entrySet();
		if (entries.isEmpty()) {
			builder.translate(key("data.exact.any"), Styler::condition);
		} else if (entries.size() == 1) {
			Map.Entry<DataComponentType<?>, Optional<?>> entry = entries.iterator().next();
			Optional<?> value = entry.getValue();
			if (value.isEmpty()) {
				ResourceLocation key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey());
				if (key != null) {
					builder.translate(key("data.exact.any_for_type"), Styler::condition, Styler.property(Component.literal(key.toString())));
				} else {
					builder.translate(key("data.unknown"), Styler::condition);
				}
				return;
			}

			ExactDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue().orElseThrow(), state, builder);
		} else {
			CompositeContainerComponent.Builder listBuilder = CompositeContainerComponent.builder()
					.vertical()
					.literal(":", Styler::condition);
			for (Map.Entry<DataComponentType<?>, Optional<?>> entry : entries) {
				Optional<?> value = entry.getValue();
				CompositeContainerComponent.Builder termBuilder = CompositeContainerComponent.builder()
						.space()
						.literal("• ", Styler::condition);
				if (value.isEmpty()) {
					ResourceLocation key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey());
					if (key != null) {
						termBuilder.translate(key("data.exact.any_for_type"), Styler::condition, Styler.property(Component.literal(key.toString())));
					} else {
						termBuilder.translate(key("data.unknown"), Styler::condition);
					}
				} else {
					ExactDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue().orElse(null), state, termBuilder);
				}
				listBuilder.component(termBuilder.build());
			}
			builder.component(listBuilder.build());
		}
	}

	static void addSubPredicatesToBuilder(ClientLevel level, Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (state.doPrefixSpace()) {
			builder.space();
		}
		builder.translate(key("data.partial"), Styler::condition);

		if (subPredicates.isEmpty()) {
			builder.translate(key("data.unknown"));
		} else if (subPredicates.size() == 1) {
			Map.Entry<ItemSubPredicate.Type<?>, ItemSubPredicate> entry = subPredicates.entrySet().iterator().next();
			PartialDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue(), state, builder);
		} else {
			CompositeContainerComponent.Builder listBuilder = CompositeContainerComponent.builder()
					.vertical()
					.literal(":", Styler::condition);
			for (Map.Entry<ItemSubPredicate.Type<?>, ItemSubPredicate> entry : subPredicates.entrySet()) {
				CompositeContainerComponent.Builder termBuilder = CompositeContainerComponent.builder()
						.space()
						.literal("• ", Styler::condition);
				PartialDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue(), state, builder);
				listBuilder.component(termBuilder.build());
			}
			builder.component(listBuilder.build());
		}
	}
	*///?}

	static void addCountToBuilder(ClientLevel level, MinMaxBounds.Ints count, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		CompositeContainerComponent.Builder countBuilder = CompositeContainerComponent.builder().space();
		addMinMaxToBuilder(key("count"), false, count, state, countBuilder);
		builder.component(countBuilder.build());
	}
}
