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
        compiledPatterns.putIfAbsent(this.namespace(), Pattern.compile(this.namespace()));
        Pattern pattern = compiledPatterns.get(this.namespace());
        return pattern.matcher(namespace).matches();
    }

    private boolean matchesPath(String path) {
        compiledPatterns.putIfAbsent(this.path(), Pattern.compile(this.path()));
        Pattern pattern = compiledPatterns.get(this.path());
        return pattern.matcher(path).matches();
    }
}
