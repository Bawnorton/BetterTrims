package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;

import java.util.List;

public final class BeehiveBlockEntityOccupantListExactAdder implements ExactAdder<List<BeehiveBlockEntity.Occupant>> {
	@Override
	public void addToBuilder(ClientLevel level, List<BeehiveBlockEntity.Occupant> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
