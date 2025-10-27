package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.predicates.DamagePredicate;
import net.minecraft.client.multiplayer.ClientLevel;

public final class DamagePredicatePartialAdder implements PartialAdder</*$ damage_predicate >>*/ DamagePredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ damage_predicate >>*/ DamagePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		MinMaxBounds.Ints damage = predicate.damage();
		MinMaxBounds.Ints durability = predicate.durability();

		builder.space();
		boolean useAnd = false;
		if (!damage.isAny()) {
			PredicateTooltip.addMinMaxToBuilder(key("damage.max_damage"), false, damage, state, builder);
			useAnd = true;
		}
		if (!durability.isAny()) {
			PredicateTooltip.addMinMaxToBuilder(key("damage.durability"), useAnd, durability, state, builder);
		}
	}
}
