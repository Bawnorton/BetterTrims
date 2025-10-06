package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.core.component.predicates.WrittenBookPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;

import java.util.Optional;

public final class WrittenBookPartialAdder implements PartialAdder</*$ written_book_predicate >>*/ WrittenBookPredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ written_book_predicate >>*/ WrittenBookPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<CollectionPredicate<Filterable<Component>, /*$ written_book_predicate >>*/ WrittenBookPredicate .PagePredicate>> pages = predicate.pages();
		if (pages.isPresent()) {
			addCollectionToBuilder(
					level,
					pages.orElseThrow(),
					"written_book",
					(ignoredLevel, pagePredicate, predicateState, collectionBuilder) -> {
						Component contents = pagePredicate.contents();
						collectionBuilder.translate(key("written_book.page.contains"), Styler::condition, Styler.value(contents.copy()));
					},
					state,
					builder
			);
		} else {
			builder.space().translate(key("written_book.any"), Styler::condition);
		}
	}
}
