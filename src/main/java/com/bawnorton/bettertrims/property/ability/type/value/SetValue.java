package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record SetValue(CountBasedValue value) implements TrimValueAbility {
    public static final MapCodec<SetValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("value").forGetter(SetValue::value)
    ).apply(instance, SetValue::new));

    @Override
    public float process(int count, RandomSource random, float value) {
        return this.value.calculate(count);
    }

    @Override
    public MapCodec<? extends TrimValueAbility> codec() {
        return CODEC;
    }
}