package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.client.tooltip.Styler;
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

public record AddValue(CountBasedValue value) implements TrimValueAbility {
    public static final MapCodec<AddValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("value").forGetter(AddValue::value)
    ).apply(instance, AddValue::new));

    @Override
    public float process(int count, RandomSource random, float value) {
        return value + this.value.calculate(count);
    }

    @Override
    public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
        return CompositeContainerComponent.builder()
            .translate("bettertrims.tooltip.ability.add_value.add", Styler::positive)
            .cycle(builder -> this.value.getValueComponents(4, includeCount, f -> Component.literal("%.1f".formatted(f))).forEach(builder::textComponent))
            .translate("bettertrims.tooltip.ability.add_value.to_value", Styler::positive)
            .spaced()
            .build();
    }

    @Override
    public MapCodec<? extends TrimValueAbility> codec() {
        return CODEC;
    }
}

    