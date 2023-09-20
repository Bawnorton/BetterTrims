package com.bawnorton.bettertrims.util;

public class NumberWrapper extends Wrapper<Number> {
    protected NumberWrapper(Number value) {
        super(value);
    }

    public static NumberWrapper zero() {
        return of(0);
    }

    public static NumberWrapper one() {
        return of(1);
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

    public int getInt() {
        return get().intValue();
    }
}
