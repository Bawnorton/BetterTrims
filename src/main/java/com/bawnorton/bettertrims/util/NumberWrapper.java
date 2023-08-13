package com.bawnorton.bettertrims.util;

public class NumberWrapper extends Wrapper<Number> {
    public NumberWrapper(Number value) {
        super(value);
    }

    public static NumberWrapper of(Number value) {
        return new NumberWrapper(value);
    }

    public void increment(Number amount) {
        set(getDouble() + amount.doubleValue());
    }

    public void decrement(Number amount) {
        set(getDouble() - amount.doubleValue());
    }

    public float getFloat() {
        return get().floatValue();
    }

    public double getDouble() {
        return get().doubleValue();
    }
}
