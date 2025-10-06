package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Formatter;
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

public record RemoveBinomial(CountBasedValue chance) implements TrimValueAbility {
	public static final MapCodec<RemoveBinomial> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("chance").forGetter(RemoveBinomial::chance)
	).apply(instance, RemoveBinomial::new));

	@Override
	public float process(int count, RandomSource random, float value) {
		float chance = this.chance.calculate(count);
		int removed = 0;
		for (int i = 0; i < value; i++) {
			if (random.nextFloat() <= chance) {
				removed++;
			}
		}
		return value - removed;
	}

	@Override
	public MapCodec<? extends TrimValueAbility> codec() {
		return CODEC;
	}

	public static final class TooltipProvider implements TrimElementTooltipProvider<RemoveBinomial> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, RemoveBinomial element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.cycle(builder -> element.chance().getValueComponents(4, includeCount, Formatter::percentage).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.remove_binomial", Styler::positive)
					.build();
		}
	}
}