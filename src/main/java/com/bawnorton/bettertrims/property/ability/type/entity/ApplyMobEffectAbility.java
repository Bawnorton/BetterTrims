package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record ApplyMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier, CountBasedValue duration) implements TrimEntityAbility {
    public static final MapCodec<ApplyMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MobEffect.CODEC.fieldOf("effect").forGetter(ApplyMobEffectAbility::effect),
        CountBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffectAbility::amplifier),
        CountBasedValue.CODEC.fieldOf("duration").forGetter(ApplyMobEffectAbility::duration)
    ).apply(instance, ApplyMobEffectAbility::new));

    private MobEffectInstance getMobEffectInstance(int count) {
        return new MobEffectInstance(effect, (int) this.duration.calculate(count), (int) amplifier.calculate(count));
    }

    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        if(target instanceof LivingEntity living) {
            living.addEffect(getMobEffectInstance(items.size()));
        }
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return CODEC;
    }
}