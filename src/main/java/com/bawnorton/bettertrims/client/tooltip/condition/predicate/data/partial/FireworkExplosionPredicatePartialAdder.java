package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.FireworkExplosionPredicate;

public final class FireworkExplosionPredicatePartialAdder implements PartialAdder</*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate > {
	private final FireworkPredicateAdder predicateAdder = new FireworkPredicateAdder();

	@Override
	public void addToBuilder(ClientLevel level,/*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		predicateAdder.addToBuilder(level, predicate.predicate(), state, builder);
	}
}
