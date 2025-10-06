package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.core.component.predicates.BundlePredicate;
import net.minecraft.client.multiplayer.ClientLevel;

public final class BundlePredicatePartialAdder extends ItemHolderPartialAdder</*$ bundle_predicate >>*/ BundlePredicate > {
	@Override
	public void addToBuilder(ClientLevel level,/*$ bundle_predicate >>*/ BundlePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		addItemHolderToBuilder(level, predicate.items().orElse(null), "bundle", state, builder);
	}
}
