//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.InstrumentComponent;

public final class InstrumentComponentExactAdder implements ExactAdder<InstrumentComponent> {
	@Override
	public void addToBuilder(ClientLevel level, InstrumentComponent object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {

	}
}
//?}