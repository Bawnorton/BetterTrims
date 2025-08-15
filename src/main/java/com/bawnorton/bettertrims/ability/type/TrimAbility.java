package com.bawnorton.bettertrims.ability.type;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;

public interface TrimAbility {
    Codec<TrimAbility> CODEC = TrimAbilityType.REGISTRY.byNameCodec().dispatch(TrimAbility::getType, TrimAbilityType::codec);

    TrimAbilityType<? extends TrimAbility> getType();

    void onAdded(LivingEntity livingEntity, int newCount);

    void onRemoved(LivingEntity livingEntity, int priorCount);
}
