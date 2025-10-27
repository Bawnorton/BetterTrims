//? if >=1.21.10 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.TypedEntityData;

public record TypedEntityDataExactAdder<T>(String prefix) implements ExactAdder<TypedEntityData<T>> {
	@Override
	public void addToBuilder(ClientLevel level, TypedEntityData<T> typedEntityData, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
//?}