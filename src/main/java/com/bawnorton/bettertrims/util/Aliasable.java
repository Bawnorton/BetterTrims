package com.bawnorton.bettertrims.util;

public final class Aliasable<T> {
    private Holder<T> holder;

    public void setHolder(T value, boolean alias) {
        holder = new Holder<>(value, alias);
    }

    public boolean isUsingAlias() {
        return holder.isAlias();
    }

    public T get() {
        return holder.value();
    }

    private record Holder<T>(T value, boolean isAlias) {}
}
