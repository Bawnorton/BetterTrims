package com.bawnorton.bettertrims.ability;

import com.bawnorton.bettertrims.ability.type.TrimAbility;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.LivingEntity;

public record TrimAbilityContainer(ApplicationPredicate predicate, TrimAbility ability) {
    public static final Codec<TrimAbilityContainer> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ApplicationPredicate.CODEC.fieldOf("applies_to").forGetter(TrimAbilityContainer::predicate),
            TrimAbility.CODEC.fieldOf("ability").forGetter(TrimAbilityContainer::ability)
    ).apply(instance, TrimAbilityContainer::new));

    public static final Codec<Holder<TrimAbilityContainer>> CODEC = RegistryFixedCodec.create(BetterTrimsRegistries.TRIM_ABILITY);

    public int getMatchingCount(LivingEntity livingEntity) {
        return predicate().apply(livingEntity);
    }

    public void onAdded(LivingEntity livingEntity) {
        int count = getMatchingCount(livingEntity);
        if (count > 0) {
            ability.onAdded(livingEntity, count);
        }
    }

    public void onRemoved(LivingEntity livingEntity, int priorCount) {
        ability.onRemoved(livingEntity, priorCount);
    }
}
