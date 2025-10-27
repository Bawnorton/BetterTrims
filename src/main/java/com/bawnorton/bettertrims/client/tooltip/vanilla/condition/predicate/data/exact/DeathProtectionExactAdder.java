//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

public final class DeathProtectionExactAdder implements ExactAdder<DeathProtection> {
	@Override
	public void addToBuilder(ClientLevel level, DeathProtection protection, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		List<ConsumeEffect> deathEffects = protection.deathEffects();
		builder.space()
				.translate(key("death_protection"), Styler::condition)
				.space();
		if (deathEffects.isEmpty()) {
			builder.translate(key("death_protection.no_effects"), Styler::condition);
		} else {
			builder.cycle(cycleBuilder -> deathEffects.forEach(effect -> {
				CompositeContainerComponent.Builder effectBuilder = CompositeContainerComponent.builder();
				// TODO
				switch (effect) {
					case ApplyStatusEffectsConsumeEffect applyStatus -> {
						List<MobEffectInstance> effects = applyStatus.effects();
					}
					default -> {
					}
				}
				cycleBuilder.component(effectBuilder.build());
			}));
		}
	}
}
//?}