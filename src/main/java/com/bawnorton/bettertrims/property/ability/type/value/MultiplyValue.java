package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

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
}