package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.CountBasedValue;
import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

public record EffectTrimAbility(List<TrimMobEffect> effects) implements TrimAbility {
    public static final MapCodec<EffectTrimAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TrimMobEffect.CODEC.listOf().fieldOf("effects").forGetter(EffectTrimAbility::effects)
    ).apply(instance, EffectTrimAbility::new));

    public static EffectTrimAbility multiple(TrimMobEffect... effects) {
        return new EffectTrimAbility(List.of(effects));
    }

    public static EffectTrimAbility single(Holder<MobEffect> effect, CountBasedValue amplifier) {
        return multiple(effect(effect, amplifier));
    }

    public static TrimMobEffect effect(Holder<MobEffect> effect, CountBasedValue amplifier) {
        return TrimMobEffect.create(effect, amplifier);
    }

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.EFFECT;
    }

    @Override
    public void start(LivingEntity livingEntity, int newCount) {
        for (TrimMobEffect effect : effects) {
            livingEntity.addEffect(effect.getMobEffectInstance(newCount));
        }
    }

    @Override
    public void stop(LivingEntity livingEntity, int priorCount) {
        for (TrimMobEffect effect : effects) {
            livingEntity.removeEffect(effect.effect());
        }
    }

    public record TrimMobEffect(Holder<MobEffect> effect, CountBasedValue amplifier) {
        public static final Codec<TrimMobEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MobEffect.CODEC.fieldOf("id").forGetter(TrimMobEffect::effect),
                CountBasedValue.CODEC.fieldOf("amplifier").forGetter(TrimMobEffect::amplifier)
        ).apply(instance, TrimMobEffect::new));

        public static TrimMobEffect create(Holder<MobEffect> effect, CountBasedValue amplifier) {
            return new TrimMobEffect(effect, amplifier);
        }

        public MobEffectInstance getMobEffectInstance(int count) {
            return new MobEffectInstance(effect, -1, (int) amplifier.calculate(count), false, false, true);
        }
    }
}
