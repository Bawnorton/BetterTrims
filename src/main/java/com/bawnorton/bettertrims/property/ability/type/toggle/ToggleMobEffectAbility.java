package com.bawnorton.bettertrims.property.ability.type.toggle;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public record ToggleMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier) implements TrimToggleAbility {
    public static final MapCodec<ToggleMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MobEffect.CODEC.fieldOf("effect").forGetter(ToggleMobEffectAbility::effect),
        CountBasedValue.CODEC.fieldOf("amplifier").forGetter(ToggleMobEffectAbility::amplifier)
    ).apply(instance, ToggleMobEffectAbility::new));

    private MobEffectInstance getMobEffectInstance(int count) {
        return new MobEffectInstance(effect, -1, (int) amplifier.calculate(count));
    }

    @Override
    public void start(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
        wearer.addEffect(getMobEffectInstance(items.size()));
    }

    @Override
    public void stop(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
        wearer.removeEffect(effect);
    }

    @Override
    public MapCodec<? extends TrimToggleAbility> codec() {
        return CODEC;
    }
}