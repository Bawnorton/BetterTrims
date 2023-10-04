package com.bawnorton.bettertrims.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public record RegexPath(String path) {
    private static final Map<String, Pattern> compiledPatterns = new HashMap<>();

    public boolean matches(@Nullable Identifier other) {
        if (other == null) return false;

        return matches(other.getPath());
    }

    public boolean matchesAny(Iterable<Identifier> others) {
        for (Identifier other : others) {
            if (matches(other)) return true;
        }
        return false;
    }

    private boolean matches(String path) {
        Pattern pattern = compiledPatterns.computeIfAbsent(this.path(), Pattern::compile);
        return pattern.matcher(path).matches();
    }

    public RegexPath withSuffix(String suffix) {
        return fromString(path + suffix);
    }

    @Override
    public String toString() {
        return "RegexPath{" +
                "path='" + path + '\'' +
                '}';
    }

    public static RegexPath fromString(String string) {
        return new RegexPath(string);
    }
}
