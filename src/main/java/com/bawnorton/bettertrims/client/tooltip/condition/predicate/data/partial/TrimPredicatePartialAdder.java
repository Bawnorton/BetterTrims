package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.TrimTooltipPage;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.TrimPredicate;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.Optional;

public final class TrimPredicatePartialAdder implements PartialAdder</*$ trim_predicate >>*/ TrimPredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ trim_predicate >>*/ TrimPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<HolderSet<TrimPattern>> pattern = predicate.pattern();
		Optional<HolderSet<TrimMaterial>> material = predicate.material();
		if (pattern.isEmpty() && material.isEmpty()) {
			builder.space().translate(key("armor_trim.any"), Styler::condition);
			return;
		}

		builder.space()
				.translate(key("armor_trim.matches"), Styler::condition)
				.space()
				.centred()
				.component(TrimTooltipPage.generateMatcherComponent(level, material.orElse(HolderSet.direct()), pattern.orElse(HolderSet.direct())));
	}
}
