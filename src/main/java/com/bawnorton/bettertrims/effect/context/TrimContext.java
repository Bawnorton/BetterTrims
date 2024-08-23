package com.bawnorton.bettertrims.effect.context;

public class TrimContext {
    private final TrimContextParameterSet parameterSet;

    public TrimContext(TrimContextParameterSet.Builder parameterSetBuilder) {
        this.parameterSet = parameterSetBuilder.build();
    }

    public static TrimContext empty() {
        return new TrimContext(TrimContextParameterSet.builder());
    }

    public boolean has(TrimContextParameter<?> parameter) {
        return parameterSet.has(parameter);
    }

    public <T> T get(TrimContextParameter<T> parameter) {
        return parameterSet.get(parameter);
    }
}
