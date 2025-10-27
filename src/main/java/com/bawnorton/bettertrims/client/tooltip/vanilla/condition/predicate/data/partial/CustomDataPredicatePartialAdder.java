package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
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
