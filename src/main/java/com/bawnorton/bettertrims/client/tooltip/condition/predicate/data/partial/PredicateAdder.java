package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.PartialDataComponentPredicateTooltipAdders;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.function.Predicate;

public interface PredicateAdder<T extends Predicate<?>> {
	void addToBuilder(ClientLevel level, T predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder);

	default String key(String key) {
		return PartialDataComponentPredicateTooltipAdders.key(key);
	}
}