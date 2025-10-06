package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public final class RecipeResourceKeyListExactAdder implements ExactAdder<List<ResourceKey<Recipe<?>>>> {
	@Override
	public void addToBuilder(ClientLevel level, List<ResourceKey<Recipe<?>>> object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		// TODO
	}
}
