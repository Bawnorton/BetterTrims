package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact.ExactAdder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.LodestoneTracker;

public final class LodestoneTrackerExactAdder implements ExactAdder<LodestoneTracker> {
	@Override
	public void addToBuilder(ClientLevel level, LodestoneTracker object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
