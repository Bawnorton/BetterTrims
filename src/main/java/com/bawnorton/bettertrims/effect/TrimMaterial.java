package com.bawnorton.bettertrims.effect;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TrimMaterial {
    private final List<String> matches;

    private TrimMaterial(List<String> matches) {
        this.matches = matches;
    }

    public static TrimMaterial of(String... matches) {
        return new TrimMaterial(List.of(matches));
    }

    @SafeVarargs
    public static TrimMaterial of(RegistryKey<ArmorTrimMaterial>... material) {
        return new TrimMaterial(List.of(Arrays.stream(material)
                                              .map(RegistryKey::getValue)
                                              .map(Identifier::getPath)
                                              .toArray(String[]::new)));
    }

    public static TrimMaterial of(Item... items) {
        return new TrimMaterial(List.of(Arrays.stream(items)
                                              .map(Registries.ITEM::getId)
                                              .map(Identifier::getPath)
                                              .toArray(String[]::new)));
    }

    public TrimMaterial or(TrimMaterial other) {
        List<String> matches = new ArrayList<>(this.matches);
        matches.addAll(other.matches);
        return new TrimMaterial(matches);
    }

    public boolean appliesTo(String... material) {
        return appliesTo(List.of(material));
    }

    public boolean appliesTo(Iterable<String> materials) {
        for (String match : matches) {
            for (String mat : materials) {
                if (match.contains(mat)) return true;
            }
        }
        return false;
    }


    public Text getTooltip() {
        return Text.translatable("bettertrims.effect.%s.tooltip".formatted(matches.get(0)));
    }
}
