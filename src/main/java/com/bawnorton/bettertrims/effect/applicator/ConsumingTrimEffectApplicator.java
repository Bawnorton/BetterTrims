package com.bawnorton.bettertrims.effect.applicator;

import com.bawnorton.bettertrims.effect.context.TrimContext;
import net.minecraft.entity.LivingEntity;

public interface ConsumingTrimEffectApplicator extends TrimEffectApplicator<Void> {
    @Override
    default Void apply(TrimContext context, LivingEntity entity) {
        consume(context, entity);
        return null;
    }

    void consume(TrimContext context, LivingEntity entity);
}
