package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record RemoveBinomial(CountBasedValue chance) implements TrimValueAbility {
    public static final MapCodec<RemoveBinomial> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("chance").forGetter(RemoveBinomial::chance)
    ).apply(instance, RemoveBinomial::new));

    @Override
    public float process(int count, RandomSource random, float value) {
        float chance = this.chance.calculate(count);
        int removed = 0;
        for(int i = 0; i < value; i++) {
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
}