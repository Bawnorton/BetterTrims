package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Formatter;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.FireworkExplosionPredicate;
import net.minecraft.core.component.predicates.FireworksPredicate;
import net.minecraft.world.item.component.FireworkExplosion;

import java.util.Optional;

public final class FireworksPredicatePartialAdder implements PartialAdder</*$ fireworks_predicate >>*/ FireworksPredicate > {
	private final FireworkPredicateAdder predicateAdder = new FireworkPredicateAdder();

	@Override
	public void addToBuilder(ClientLevel level, /*$ fireworks_predicate >>*/ FireworksPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		MinMaxBounds.Ints flightDuration = predicate.flightDuration();
		Optional<CollectionPredicate<FireworkExplosion, /*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate .FireworkPredicate>> explosions = predicate.explosions();

		if (!flightDuration.isAny()) {
			builder.space()
					.translate(key("fireworks.flight_duration"), Styler::condition);
			PredicateTooltip.addMinMaxToBuilder(
					key("fireworks.flight_duration"),
					false,
					flightDuration,
					v -> Formatter.decimal(v).append("s"),
					state,
					builder
			);
			builder.space()
					.translate("bettertrims.tooltip.and", Styler::condition);
		} else {
			builder.space()
					.translate(key("fireworks.flight_duration.any"), Styler::condition);
		}

		builder.cycle(cycleBuilder -> {
			CollectionPredicate<FireworkExplosion, /*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate .FireworkPredicate> fireworkCollection = explosions.orElseThrow();
			CompositeContainerComponent.Builder fireworkBuilder = CompositeContainerComponent.builder();
			addCollectionToBuilder(level, fireworkCollection, "fireworks", predicateAdder, state, fireworkBuilder);
			cycleBuilder.component(fireworkBuilder.build());
		});
	}
}
