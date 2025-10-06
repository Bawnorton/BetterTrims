package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.WritableBookPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;

import java.util.Optional;

public final class WritableBookPartialAdder implements PartialAdder</*$ writable_book_predicate >>*/ WritableBookPredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ writable_book_predicate >>*/ WritableBookPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<CollectionPredicate<Filterable<String>, /*$ writable_book_predicate >>*/ WritableBookPredicate .PagePredicate>> pages = predicate.pages();
		builder.space().translate(key("writable_book"), Styler::condition);
		if (pages.isPresent()) {
			addCollectionToBuilder(
					level,
					pages.orElseThrow(),
					"writable_book",
					(ignoredLevel, pagePredicate, predicateState, collectionBuilder) -> {
						String contents = pagePredicate.contents();
						if (contents.isEmpty()) {
							collectionBuilder.translate(key("writable_book.page.any"), Styler::condition);
						} else {
							collectionBuilder.translate(
									key("writable_book.page.contains"),
									Styler::condition,
									Styler.value(Component.literal("\"%s\"".formatted(contents)))
							);
						}
					},
					state,
					builder
			);
		}
	}
}
