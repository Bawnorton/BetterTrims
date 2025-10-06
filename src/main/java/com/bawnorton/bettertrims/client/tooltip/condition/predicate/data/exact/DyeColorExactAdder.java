package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.DyeColor;

public final class DyeColorExactAdder implements ExactAdder<DyeColor> {
	@Override
	public void addToBuilder(ClientLevel level, DyeColor object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {

	}
}
