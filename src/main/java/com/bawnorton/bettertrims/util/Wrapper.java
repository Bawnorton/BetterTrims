package com.bawnorton.bettertrims.util;

public class Wrapper<T> {
    protected T value;

    public Wrapper(T value) {
        this.value = value;
    }

    public static <T> Wrapper<T> of(T value) {
        return new Wrapper<>(value);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "null";
        return "Wrapper{" +
                "value=" + value +
                '}';
    }
}