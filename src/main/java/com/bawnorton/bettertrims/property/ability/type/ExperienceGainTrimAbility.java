package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.CountBasedValue;
import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;

public record ExperienceGainTrimAbility(CountBasedValue multiplier) implements TrimAbility {
    public static final MapCodec<ExperienceGainTrimAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CountBasedValue.CODEC.fieldOf("multiplier").forGetter(ExperienceGainTrimAbility::multiplier)
    ).apply(instance, ExperienceGainTrimAbility::new));

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.EXPERIENCE_GAIN;
    }

    @Override
    public int modifyGainedExperience(LivingEntity wearer, int amount, int count) {
        return Math.round(multiplier.calculate(count) * amount);
    }
}
