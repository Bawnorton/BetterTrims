package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeBuilder;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PredicateTooltip {
    static String key(String key) {
        return "bettertrims.tooltip.condition.predicate.%s".formatted(key);
    }

    static void addMinMaxToBuilder(String key, boolean useAnd, MinMaxBounds<?> minMaxBounds, CompositeBuilder<?> builder) {
        addMinMaxToBuilder(key, useAnd, minMaxBounds.min().orElse(null), minMaxBounds.max().orElse(null), builder);
    }

    static void addMinMaxToBuilder(String key, boolean useAnd, Number min, Number max, CompositeBuilder<?> builder) {
        addMinMaxToBuilder(key, useAnd, min, max, value -> Component.literal("%.0f".formatted(value)), builder);
    }

    static void addMinMaxToBuilder(String key, boolean useAnd, MinMaxBounds<?> minMaxBounds, Function<Double, MutableComponent> formatter, CompositeBuilder<?> builder) {
        addMinMaxToBuilder(key, useAnd, minMaxBounds.min().orElse(null), minMaxBounds.max().orElse(null), formatter, builder);
    }

    static void addMinMaxToBuilder(String key, boolean useAnd, Number min, Number max, Function<Double, MutableComponent> formatter, CompositeBuilder<?> builder) {
        if(useAnd) {
            builder.space().translate("bettertrims.tooltip.and", Styler::condition).space();
        }
        if(min != null && max != null) {
            if(min.equals(max)) {
                builder.translate("%s.is".formatted(key), Styler::condition, Styler.number(formatter.apply(min.doubleValue())));
            } else {
                builder.translate("%s.between".formatted(key), Styler::condition, Styler.number(formatter.apply(min.doubleValue())), Styler.number(formatter.apply(max.doubleValue())));
            }
        } else if(min != null) {
            builder.translate("%s.at_least".formatted(key), Styler::condition, Styler.number(formatter.apply(min.doubleValue())));
        } else if(max != null) {
            builder.translate("%s.at_most".formatted(key), Styler::condition, Styler.number(formatter.apply(max.doubleValue())));
        }
    }

    static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, CompositeContainerComponent.Builder builder) {
        addRegisteredElementsToBuilder(level, key, registryKey, elements, (Function<T, Component>) null, builder);
    }

    static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, Function<T, Component> nameMapper, CompositeContainerComponent.Builder builder) {
        addRegisteredElementsToBuilder(level, key, registryKey, elements, (element, composite) -> nameMapper.apply(element), builder);
    }

    static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, BiFunction<T, CompositeBuilder<?>, Component> nameMapper, CompositeContainerComponent.Builder builder) {
        Registry<T> registry = level.registryAccess().lookupOrThrow(registryKey);
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(elements.size() == 1 ? key : key + ".multiple", Styler::condition)
            .space()
            .cycle(cycler -> elements.stream()
                .forEach(holder -> {
                    if(nameMapper != null) {
                        CompositeContainerComponent.Builder elementBuilder = CompositeContainerComponent.builder();
                        Component name = Styler.name(nameMapper.apply(holder.unwrap().map(registry::getValueOrThrow, Function.identity()), elementBuilder).copy());
                        cycler.component(CompositeContainerComponent.builder().textComponent(name).component(elementBuilder.build()).build());
                    } else {
                        cycler.textComponent(Styler.name(Component.literal(holder.unwrap().map(ResourceKey::location, registry::getKey).toString())));
                    }
                }))
            .build());
    }

    static <T extends Enum<T>> void addEnumListToBuilder(String key, List<T> enums, Function<T, Component> nameMapper, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key)
            .space()
            .cycle(cycler -> enums.stream()
                .map(nameMapper)
                .map(component -> (Component) Styler.name(component.copy()))
                .forEach(cycler::textComponent))
            .build());
    }
}
