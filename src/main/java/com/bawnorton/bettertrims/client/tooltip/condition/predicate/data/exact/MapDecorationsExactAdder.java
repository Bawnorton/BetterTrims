package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.MapDecorations;

public final class MapDecorationsExactAdder implements ExactAdder<MapDecorations> {
	@Override
	public void addToBuilder(ClientLevel level, MapDecorations object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
