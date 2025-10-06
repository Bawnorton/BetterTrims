package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomModelData;

import java.util.List;

public final class CustomModelDataExactAdder implements ExactAdder<CustomModelData> {
	@Override
	public void addToBuilder(ClientLevel level, CustomModelData customModelData, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		//? if >=1.21.8 {
		List<Integer> colors = customModelData.colors();
		List<Boolean> flags = customModelData.flags();
		List<Float> floats = customModelData.floats();
		List<String> strings = customModelData.strings();
		if (colors.isEmpty() && flags.isEmpty() && floats.isEmpty() && strings.isEmpty()) {
			builder.translate(key("custom_model_data.any"), Styler::condition);
		} else {
			CompositeContainerComponent.Builder customModelDataBuilder = CompositeContainerComponent.builder()
					.space()
					.translate(key("custom_model_data"), Styler::condition)
					.space()
					.cycle(cycleBuilder -> {
						int maxSize = Math.max(Math.max(colors.size(), flags.size()), Math.max(floats.size(), strings.size()));
						for (int i = 0; i < maxSize; i++) {
							CompositeContainerComponent.Builder entryBuilder = CompositeContainerComponent.builder();
							entryBuilder.space()
									.translate(key("custom_model_data.index"), Styler::condition, Styler.number(i))
									.space();
							if (i < colors.size()) {
								entryBuilder.translate(
										key("custom_model_data.color"),
										Styler::condition,
										Styler.number(colors.get(i))
								);
							}
							if (i < flags.size()) {
								if (i < colors.size()) entryBuilder.literal(",", Styler::condition).space();
								entryBuilder.translate(
										key("custom_model_data.flag"),
										Styler::condition,
										Styler.value(Component.literal(String.valueOf(flags.get(i))))
								);
							}
							if (i < floats.size()) {
								if (i < colors.size() || i < flags.size())
									entryBuilder.literal(",", Styler::condition).space();
								entryBuilder.translate(
										key("custom_model_data.float"),
										Styler::condition,
										Styler.number(floats.get(i))
								);
							}
							if (i < strings.size()) {
								if (i < colors.size() || i < flags.size() || i < floats.size())
									entryBuilder.literal(",", Styler::condition).space();
								entryBuilder.translate(
										key("custom_model_data.string"),
										Styler::condition,
										Styler.value(Component.literal(strings.get(i)))
								);
							}
							cycleBuilder.component(entryBuilder.build());
						}
					});
			builder.component(customModelDataBuilder.build());
		}
	//?} else {
	/*int value = customModelData.value();
	builder.translate(
			key("custom_model_data"),
			Styler::condition,
			Styler.number(Component.literal(String.valueOf(value)))
	);
	*///?}
	}
}
