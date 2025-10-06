//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.item.EitherHolder;

public class ChickenVariantEitherHolderExactAdder implements ExactAdder<EitherHolder<ChickenVariant>> {
	@Override
	public void addToBuilder(ClientLevel level, EitherHolder<ChickenVariant> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {

	}
}
//?}