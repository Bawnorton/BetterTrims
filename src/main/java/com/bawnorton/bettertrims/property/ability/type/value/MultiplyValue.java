package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public record MultiplyValue(CountBasedValue value) implements TrimValueAbility {
	public static final MapCodec<MultiplyValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("value").forGetter(MultiplyValue::value)
	).apply(instance, MultiplyValue::new));

	@Override
	public float process(int count, RandomSource random, float value) {
		return value * this.value.calculate(count);
	}

	@Override
	public MapCodec<? extends TrimValueAbility> codec() {
		return CODEC;
	}

	public static final class TooltipProvider implements TrimElementTooltipProvider<MultiplyValue> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, MultiplyValue element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.multiply_value.multiply", Styler::positive)
					.cycle(builder -> element.value().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.translate("bettertrims.tooltip.ability.multiply_value.to_value", Styler::positive)
					.spaced()
					.build();
		}
	}
}