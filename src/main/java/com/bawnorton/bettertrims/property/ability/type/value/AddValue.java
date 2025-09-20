package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record AddValue(CountBasedValue value) implements TrimValueAbility {
    public static final MapCodec<AddValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("value").forGetter(AddValue::value)
    ).apply(instance, AddValue::new));

    @Override
    public float process(int count, RandomSource random, float value) {
        return value + this.value.calculate(count);
    }

    @Override
    public MapCodec<? extends TrimValueAbility> codec() {
        return CODEC;
    }
}

    