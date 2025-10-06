package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.mixin.accessor.AdventureModePredicateAccessor;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.BlockPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.AdventureModePredicate;

import java.util.List;

public record AdventureModePredicateExactAdder(String prefix) implements ExactAdder<AdventureModePredicate> {
	@Override
	public void addToBuilder(ClientLevel level, AdventureModePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		AdventureModePredicateAccessor accessor = (AdventureModePredicateAccessor) predicate;
		List<BlockPredicate> blockPredicates = accessor.bettertrims$predicates();
		if (blockPredicates.isEmpty()) {
			builder.translate(key("%s.any".formatted(prefix)), Styler::condition);
		} else {
			CompositeContainerComponent.Builder canBreakBuilder = CompositeContainerComponent.builder()
					.space()
					.translate(key(prefix), Styler::condition)
					.space()
					.cycle(cycleBuilder -> blockPredicates.forEach(blockPredicate -> {
						CompositeContainerComponent.Builder blockBuilder = CompositeContainerComponent.builder();
						BlockPredicateTooltip.addToBuilder(level, blockPredicate, state, blockBuilder);
						cycleBuilder.component(blockBuilder.build());
					}));
			builder.component(canBreakBuilder.build());
		}
	}
}
