//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.frog.FrogVariant;

public final class FrogVariantHolderExactAdder implements ExactAdder<Holder<FrogVariant>> {
	@Override
	public void addToBuilder(ClientLevel level, Holder<FrogVariant> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
//?}