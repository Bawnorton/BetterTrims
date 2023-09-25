package com.bawnorton.bettertrims.util;

public class Wrapper<T> {
    protected T value;

    protected Wrapper(T value) {
        this.value = value;
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

        return "Wrapper{" + "type=" + value + '}';
    }
}
