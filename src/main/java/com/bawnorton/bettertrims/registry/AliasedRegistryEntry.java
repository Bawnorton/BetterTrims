package com.bawnorton.bettertrims.registry;

import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

public final class AliasedRegistryEntry<T> {
    private EntryHolder<T> entryHolder;

    public void init(EntryHolder<T> holder) {
        this.entryHolder = holder;
    }

    public boolean isUsingAlias() {
        if(entryHolder == null) throw new IllegalStateException("Not initialized");

        return entryHolder.isAlias();
    }

    public @NotNull RegistryEntry<T> getEntry() {
        if(entryHolder == null) throw new IllegalStateException("Not initialized");

        return entryHolder.entry();
    }
    public record EntryHolder<T>(RegistryEntry<T> entry, boolean isAlias) {}
}
