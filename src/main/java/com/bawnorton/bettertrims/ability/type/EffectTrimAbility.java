package com.bawnorton.bettertrims.ability.type;

import com.bawnorton.bettertrims.ability.CountBasedValue;
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

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.EFFECT;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, int newCount) {
        for (TrimMobEffect effect : effects) {
            livingEntity.addEffect(effect.getMobEffectInstance(newCount));
        }
    }

    @Override
    public void onRemoved(LivingEntity livingEntity, int priorCount) {
        for (TrimMobEffect effect : effects) {
            livingEntity.removeEffect(effect.effectHolder());
        }
    }

    public record TrimMobEffect(Holder<MobEffect> effectHolder, CountBasedValue amplifier) {
        public static final Codec<TrimMobEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MobEffect.CODEC.fieldOf("id").forGetter(TrimMobEffect::effectHolder),
                CountBasedValue.CODEC.fieldOf("amplifier").forGetter(TrimMobEffect::amplifier)
        ).apply(instance, TrimMobEffect::new));

        public MobEffectInstance getMobEffectInstance(int count) {
            return new MobEffectInstance(effectHolder, -1, (int) amplifier.calculate(count));
        }
    }
}
