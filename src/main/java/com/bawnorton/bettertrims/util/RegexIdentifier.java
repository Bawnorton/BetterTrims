package com.bawnorton.bettertrims.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public record RegexIdentifier(String namespace, String path) {
    private static final Map<String, Pattern> compiledPatterns = new HashMap<>();

    public boolean matches(@Nullable Identifier other) {
        if (other == null) return false;

        return matches(other.getNamespace(), other.getPath());
    }

    public boolean matchesAny(Iterable<Identifier> others) {
        for (Identifier other : others) {
            if (matches(other)) return true;
        }
        return false;
    }

    private boolean matches(String namespace, String path) {
        return matchesNamespace(namespace) && matchesPath(path);
    }

    private boolean matchesNamespace(String namespace) {
        Pattern pattern = compiledPatterns.computeIfAbsent(this.namespace(), Pattern::compile);
        return pattern.matcher(namespace).matches();
    }

    private boolean matchesPath(String path) {
        Pattern pattern = compiledPatterns.computeIfAbsent(this.path(), Pattern::compile);
        return pattern.matcher(path).matches();
    }

    public RegexIdentifier withSuffixedPath(String suffix) {
        return new RegexIdentifier(namespace, path + suffix);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    public static RegexIdentifier fromString(String string) {
        String[] split = string.split(":");
        if (split.length != 2) throw new IllegalArgumentException("Invalid identifier string: " + string);

        return new RegexIdentifier(split[0], split[1]);
    }
}
