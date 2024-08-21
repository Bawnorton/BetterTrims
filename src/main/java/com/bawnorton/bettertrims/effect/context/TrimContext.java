package com.bawnorton.bettertrims.effect.context;

import net.minecraft.entity.LivingEntity;

public class TrimContext {
    private final LivingEntity entity;
    private final TrimContextParameterSet parameterSet;

    public TrimContext(LivingEntity entity, TrimContextParameterSet.Builder parameterSetBuilder) {
        this.entity = entity;
        this.parameterSet = parameterSetBuilder.build();
    }

    public TrimContext(LivingEntity entity) {
        this(entity, TrimContextParameterSet.builder());
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public boolean has(TrimContextParameter<?> parameter) {
        return parameterSet.has(parameter);
    }

    public <T> T get(TrimContextParameter<T> parameter) {
        return parameterSet.get(parameter);
    }
}
