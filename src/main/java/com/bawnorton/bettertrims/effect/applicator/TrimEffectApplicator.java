package com.bawnorton.bettertrims.effect.applicator;

import com.bawnorton.bettertrims.effect.context.TrimContext;

public interface TrimEffectApplicator<T> {
    static <T> TrimEffectApplicator<T> none() {
        return context -> null;
    }

    T apply(TrimContext context);
}
