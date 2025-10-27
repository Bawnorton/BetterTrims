//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DataComponentMatchersTooltip {
	static void addToBuilder(ClientLevel level, DataComponentMatchers components, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (components.isEmpty()) {
			builder.translate(key("any"), Styler::condition);
			return;
		}

		DataComponentExactPredicate exact = components.exact();
		if (!exact.isEmpty()) {
			addDataComponentExactPredicateToBuilder(level, exact, state, builder);
		}

		Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = components.partial();
		if (!partial.isEmpty()) {
			addPartialDataComponentPredicatesToBuilder(level, partial, state, builder);
		}
	}

	static String key(String key) {
		return PredicateTooltip.key("data.%s".formatted(key));
	}

	static void addDataComponentExactPredicateToBuilder(ClientLevel level, DataComponentExactPredicate exact, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Set<Map.Entry<DataComponentType<?>, Optional<?>>> entries = exact.asPatch().entrySet();
		if (entries.isEmpty()) {
			builder.translate(key("exact.any"), Styler::condition);
		} else if (entries.size() == 1) {
			Map.Entry<DataComponentType<?>, Optional<?>> entry = entries.iterator().next();
			Optional<?> value = entry.getValue();
			if (value.isEmpty()) {
				ResourceLocation key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey());
				if (key != null) {
					builder.translate(key("exact.any_for_type"), Styler::condition, Styler.property(Component.literal(key.toString())));
				} else {
					builder.translate(key("unknown"), Styler::condition);
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
						termBuilder.translate(key("exact.any_for_type"), Styler::condition, Styler.property(Component.literal(key.toString())));
					} else {
						termBuilder.translate(key("unknown"), Styler::condition);
					}
				} else {
					ExactDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue().orElse(null), state, termBuilder);
				}
				listBuilder.component(termBuilder.build());
			}
			builder.component(listBuilder.build());
		}
	}

	static void addPartialDataComponentPredicatesToBuilder(ClientLevel level, Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (partial.isEmpty()) {
			builder.translate(key("unknown"));
		} else if (partial.size() == 1) {
			Map.Entry<DataComponentPredicate.Type<?>, DataComponentPredicate> entry = partial.entrySet().iterator().next();
			PartialDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue(), state, builder);
		} else {
			CompositeContainerComponent.Builder listBuilder = CompositeContainerComponent.builder()
					.vertical()
					.literal(":", Styler::condition);
			for (Map.Entry<DataComponentPredicate.Type<?>, DataComponentPredicate> entry : partial.entrySet()) {
				CompositeContainerComponent.Builder termBuilder = CompositeContainerComponent.builder()
						.space()
						.literal("• ", Styler::condition);
				PartialDataComponentPredicateTooltipAdders.addToBuilder(level, entry.getKey(), entry.getValue(), state, builder);
				listBuilder.component(termBuilder.build());
			}
			builder.component(listBuilder.build());
		}
	}
}
//?}