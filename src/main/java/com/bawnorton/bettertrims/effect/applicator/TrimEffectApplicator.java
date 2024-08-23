package com.bawnorton.bettertrims.effect.applicator;

import com.bawnorton.bettertrims.effect.context.TrimContext;
import net.minecraft.entity.LivingEntity;

public interface TrimEffectApplicator<T> {
    static <T> TrimEffectApplicator<T> none() {
        return (context, entity) -> null;
    }

    T apply(TrimContext context, LivingEntity entity);
}
