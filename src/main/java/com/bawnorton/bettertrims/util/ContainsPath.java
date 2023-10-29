package com.bawnorton.bettertrims.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record ContainsPath(String path) {
    public boolean isIn(@Nullable Identifier other) {
        if (other == null) return false;

        return isIn(other.getPath());
    }

    public boolean isInAny(Iterable<Identifier> others) {
        for (Identifier other : others) {
            if (isIn(other)) return true;
        }
        return false;
    }

    private boolean isIn(String path) {
        return path.contains(this.path);
    }

    public ContainsPath withSuffix(String suffix) {
        return fromString(path + suffix);
    }

    @Override
    public String toString() {
        return "ContainsPath{" +
                "path='" + path + '\'' +
                '}';
    }

    public static ContainsPath fromString(String string) {
        return new ContainsPath(string);
    }
}
