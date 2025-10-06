package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.PaintingVariant;

public final class PaintingVariantHolderExactAdder implements ExactAdder<Holder<PaintingVariant>> {
	@Override
	public void addToBuilder(ClientLevel level, Holder<PaintingVariant> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
