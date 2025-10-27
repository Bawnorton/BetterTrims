//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.CowVariant;

public final class CowVariantHolderExactAdder implements ExactAdder<Holder<CowVariant>> {
	@Override
	public void addToBuilder(ClientLevel level, Holder<CowVariant> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
//?}