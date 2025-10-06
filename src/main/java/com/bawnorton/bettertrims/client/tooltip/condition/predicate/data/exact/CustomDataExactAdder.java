package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.EntityPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.CustomData;

public record CustomDataExactAdder(String prefix) implements ExactAdder<CustomData> {
	@Override
	public void addToBuilder(ClientLevel level, CustomData customData, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (customData.isEmpty()) {
			builder.translate(key("custom_data.%s.any".formatted(prefix)), Styler::condition);
		} else {
			EntityPredicateTooltip.addNbtPredicateToBuilder(level, key("custom_data.%s".formatted(prefix)), new NbtPredicate(customData.copyTag()), state, builder);
		}
	}
}