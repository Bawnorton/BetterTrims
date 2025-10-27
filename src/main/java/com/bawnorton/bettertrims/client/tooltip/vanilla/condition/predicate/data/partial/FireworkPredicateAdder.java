package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.predicates.FireworkExplosionPredicate;
import net.minecraft.world.item.component.FireworkExplosion;

import java.util.Optional;

public final class FireworkPredicateAdder implements PredicateAdder</*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate .FireworkPredicate> {
		@Override
		public void addToBuilder(ClientLevel level,/*$ firework_explosion_predicate >>*/ FireworkExplosionPredicate .FireworkPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
			Optional<FireworkExplosion.Shape> shape = predicate.shape();
			Optional<Boolean> trail = predicate.trail();
			Optional<Boolean> twinkle = predicate.twinkle();

			boolean useAnd = false;
			builder.space();
			if (shape.isEmpty() && trail.isEmpty() && twinkle.isEmpty()) {
				builder.translate(key("firework.any"), Styler::condition);
				return;
			}

			builder.translate(key("firework.matches"), Styler::condition).space();

			if (shape.isPresent()) {
				builder.translate(key("firework.shape"), Styler::condition, Styler.name(shape.orElseThrow().getName()));
				useAnd = true;
			}

			if (trail.isPresent()) {
				if (useAnd) {
					builder.space()
							.translate("bettertrims.tooltip.and", Styler::condition)
							.space();
				}
				builder.translate(
						key(trail.orElseThrow() ? "firework.trail" : "firework.no_trail"),
						Styler::condition
				);
				useAnd = true;
			}

			if (twinkle.isPresent()) {
				if (useAnd) {
					builder.space()
							.translate("bettertrims.tooltip.and", Styler::condition)
							.space();
				}
				builder.translate(
						key(twinkle.orElseThrow() ? "firework.twinkle" : "firework.no_twinkle"),
						Styler::condition
				);
			}
		}
	}