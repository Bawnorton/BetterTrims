package com.bawnorton.bettertrims.util;

import java.util.function.Supplier;

public final class Memoized<T> {
    private final Supplier<T> supplier;

    private T value;

    private Memoized(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Memoized<T> of(Supplier<T> supplier) {
        return new Memoized<>(supplier);
    }

    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
