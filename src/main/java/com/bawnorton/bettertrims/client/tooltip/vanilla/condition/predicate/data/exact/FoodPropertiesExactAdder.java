package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.food.FoodProperties;

public final class FoodPropertiesExactAdder implements ExactAdder<FoodProperties> {
	@Override
	public void addToBuilder(ClientLevel level, FoodProperties food, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		boolean canAlwaysEat = food.canAlwaysEat();
		int nutrition = food.nutrition();
		float saturation = food.saturation();

		CompositeContainerComponent.Builder foodBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(key("food"), Styler::condition)
				.space()
				.translate(
						key("food.nutrition"),
						Styler::condition,
						Styler.number(nutrition)
				)
				.space()
				.translate(
						key("food.saturation"),
						Styler::condition,
						Styler.number(saturation)
				);
		if (canAlwaysEat) {
			foodBuilder.space().translate(key("food.can_always_eat"), Styler::condition);
		}
		builder.component(foodBuilder.build());
	}
}
