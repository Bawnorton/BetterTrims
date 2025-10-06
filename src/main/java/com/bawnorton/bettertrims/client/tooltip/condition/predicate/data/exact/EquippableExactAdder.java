//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.equipment.Equippable;

public final class EquippableExactAdder implements ExactAdder<Equippable> {
	@Override
	public void addToBuilder(ClientLevel level, Equippable object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {

	}
}
//?}