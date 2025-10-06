package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.ContainerPredicate;

public final class ContainerPredicatePartialAdder extends ItemHolderPartialAdder</*$ container_predicate >>*/ ContainerPredicate > {
	@Override
	public void addToBuilder(ClientLevel level,/*$ container_predicate >>*/ ContainerPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		addItemHolderToBuilder(level, predicate.items().orElse(null), "container", state, builder);
	}
}
