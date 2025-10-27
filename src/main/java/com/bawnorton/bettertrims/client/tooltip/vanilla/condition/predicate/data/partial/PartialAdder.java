package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.PartialDataComponentPredicateTooltipAdders;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.DataComponentPredicate;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface PartialAdder<T extends /*$ item_data_predicate >>*/ DataComponentPredicate > {
	Function</*$ item_data_predicate >>*/ DataComponentPredicate .Type<?>, PartialAdder<? extends /*$ item_data_predicate >>*/ DataComponentPredicate >> UNKNOWN = type -> (level, predicate, state, builder) -> builder.translate(
			PartialDataComponentPredicateTooltipAdders.key("unknown_type"),
			Styler::property,
			type.toString()
	);

	void addToBuilder(ClientLevel level, T predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder);

	default String key(String key) {
		return PartialDataComponentPredicateTooltipAdders.key(key);
	}

	default <C, P extends Predicate<C>> void addCollectionToBuilder(ClientLevel level, CollectionPredicate<C, P> collection, String key, PredicateAdder<P> contentAdder, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<CollectionContentsPredicate<C, P>> contains = collection.contains();
		Optional<CollectionCountsPredicate<C, P>> counts = collection.counts();
		Optional<MinMaxBounds.Ints> size = collection.size();
		if ((contains.isEmpty() || contains.orElseThrow().unpack().isEmpty())
				&& (counts.isEmpty() || counts.orElseThrow().unpack().isEmpty())
				&& (size.isEmpty() || size.orElseThrow().isAny())) {
			builder.space().translate(key(key + ".any"), Styler::condition);
			return;
		}

		builder.cycle(cycleBuilder -> {
			if (contains.isPresent()) {
				CollectionContentsPredicate<C, P> collectionContentsPredicate = contains.get();
				List<P> contentPredicates = collectionContentsPredicate.unpack();
				BiConsumer<CompositeContainerComponent.Builder, P> contentHeader = (contentPredicateBuilder, contentPredicate) -> {
					contentAdder.addToBuilder(level, contentPredicate, state, contentPredicateBuilder);
					cycleBuilder.component(contentPredicateBuilder.build());
				};
				if (contentPredicates.size() == 1) {
					contentHeader.accept(
							CompositeContainerComponent.builder()
									.space()
									.translate(key(key + ".single"), Styler::condition)
									.space(),
							contentPredicates.getFirst()
					);
				} else {
					for (P contentPredicate : contentPredicates) {
						contentHeader.accept(
								CompositeContainerComponent.builder()
										.space()
										.translate(key(key + ".all_of"), Styler::condition)
										.space(),
								contentPredicate
						);
					}
				}
			}

			if (counts.isPresent()) {
				CollectionCountsPredicate<C, P> collectionCountsPredicate = counts.get();
				List<CollectionCountsPredicate.Entry<C, P>> countPredicates = collectionCountsPredicate.unpack();
				BiConsumer<CompositeContainerComponent.Builder, CollectionCountsPredicate.Entry<C, P>> countHeader = (contentPredicateBuilder, countPredicateEntry) -> {
					MinMaxBounds.Ints count = countPredicateEntry.count();
					P countPredicate = countPredicateEntry.test();
					contentAdder.addToBuilder(level, countPredicate, state, contentPredicateBuilder);
					contentPredicateBuilder.literal("\"", Styler::condition).space();
					PredicateTooltip.addMinMaxToBuilder(
							key(key + ".count"),
							false,
							count,
							state.withPrefixSpace(false),
							contentPredicateBuilder
					);
					state.withPrefixSpace(true);
					cycleBuilder.component(contentPredicateBuilder.build());
				};
				if (countPredicates.size() == 1) {
					countHeader.accept(
							CompositeContainerComponent.builder()
									.space()
									.translate(key(key + ".single"), Styler::condition)
									.space()
									.literal("\"", Styler::condition),
							countPredicates.getFirst()
					);
				} else {
					for (CollectionCountsPredicate.Entry<C, P> countPredicateEntry : countPredicates) {
						countHeader.accept(
								CompositeContainerComponent.builder()
										.space()
										.translate(key(key + ".all_of"), Styler::condition)
										.space()
										.literal("\"", Styler::condition),
								countPredicateEntry
						);
					}
				}
			}

			if (size.isPresent()) {
				MinMaxBounds.Ints sizeBounds = size.orElseThrow();
				CompositeContainerComponent.Builder itemPredicateBuilder = CompositeContainerComponent.builder();
				itemPredicateBuilder.space()
						.translate(key(key + ".size"), Styler::condition);
				PredicateTooltip.addMinMaxToBuilder(
						key(key + ".size"),
						false,
						sizeBounds,
						state,
						itemPredicateBuilder
				);
				cycleBuilder.component(itemPredicateBuilder.build());
			}
		});
	}
}