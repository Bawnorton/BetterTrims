package com.bawnorton.bettertrims.effect.context;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TrimContextParameterSet {
    private final Map<TrimContextParameter<?>, Object> parameters;

    private TrimContextParameterSet(Map<TrimContextParameter<?>, Object> parameters) {
        this.parameters = parameters;
    }

    public boolean has(TrimContextParameter<?> parameter) {
        return parameters.containsKey(parameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TrimContextParameter<T> parameter) {
        Object value = parameters.get(parameter);
        if (value == null) {
            throw new NoSuchElementException(parameter.id().toString());
        }
        return (T) value;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<TrimContextParameter<?>, Object> parameters = new HashMap<>();

        public Builder add(TrimContextParameter<?> parameter, Object value) {
            parameters.put(parameter, value);
            return this;
        }

        public TrimContextParameterSet build() {
            return new TrimContextParameterSet(parameters);
        }
    }
}
