package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.CustomDataPredicate;
import net.minecraft.network.chat.Component;

public final class CustomDataPredicatePartialAdder implements PartialAdder</*$ custom_data_predicate >>*/ CustomDataPredicate > {
	@Override
	public void addToBuilder(ClientLevel level,/*$ custom_data_predicate >>*/ CustomDataPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		builder.component(CompositeContainerComponent.builder()
				.space()
				.translate(
						key("custom_data"),
						Styler::condition,
						Styler.value(Component.literal(predicate.value().tag().toString()))
				)
				.build());
	}
}
