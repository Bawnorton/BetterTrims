package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.util.Formatter;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeBuilder;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.version.VRegistry;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PredicateTooltip {
	static String key(String key) {
		return "bettertrims.tooltip.condition.predicate.%s".formatted(key);
	}

	static void addMinMaxToBuilder(String key, boolean useAnd, MinMaxBounds<?> minMaxBounds, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		addMinMaxToBuilder(key, useAnd, minMaxBounds.min().orElse(null), minMaxBounds.max().orElse(null), state, builder);
	}

	static void addMinMaxToBuilder(String key, boolean useAnd, Number min, Number max, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		addMinMaxToBuilder(key, useAnd, min, max, Formatter::decimal, state, builder);
	}

	static void addMinMaxToBuilder(String key, boolean useAnd, MinMaxBounds<?> minMaxBounds, Function<Double, MutableComponent> formatter, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		addMinMaxToBuilder(key, useAnd, minMaxBounds.min().orElse(null), minMaxBounds.max().orElse(null), formatter, state, builder);
	}

	static void addMinMaxToBuilder(String key, boolean useAnd, Number min, Number max, Function<Double, MutableComponent> formatter, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		if (state.doPrefixSpace()) {
			builder.space();
		}
		if (useAnd) {
			builder.translate("bettertrims.tooltip.and", Styler::condition).space();
		}
		if (min != null && max != null) {
			if (min.equals(max)) {
				builder.translate("%s.is".formatted(key), Styler::condition, Styler.number(formatter.apply(min.doubleValue())));
			} else {
				builder.translate(
						"%s.between".formatted(key),
						Styler::condition,
						Styler.number(formatter.apply(min.doubleValue())),
						Styler.number(formatter.apply(max.doubleValue()))
				);
			}
		} else if (min != null) {
			builder.translate("%s.at_least".formatted(key), Styler::condition, Styler.number(formatter.apply(min.doubleValue())));
		} else if (max != null) {
			builder.translate("%s.at_most".formatted(key), Styler::condition, Styler.number(formatter.apply(max.doubleValue())));
		}
	}

	static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		addRegisteredElementsToBuilder(level, key, registryKey, elements, (Function<T, Component>) null, state, builder);
	}

	static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, Function<T, Component> nameMapper, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		addRegisteredElementsToBuilder(level, key, registryKey, elements, nameMapper == null ? null : (element, composite) -> nameMapper.apply(element), state, builder);
	}

	static <T> void addRegisteredElementsToBuilder(ClientLevel level, String key, ResourceKey<Registry<T>> registryKey, HolderSet<T> elements, BiFunction<T, CompositeBuilder<?>, Component> nameMapper, LootConditionTooltips.State state, CompositeBuilder<?> builder) {
		Registry<T> registry = VRegistry.get(level, registryKey);
		CompositeContainerComponent.Builder registeredElementBuilder = CompositeContainerComponent.builder();
		if (state.doPrefixSpace()) {
			registeredElementBuilder.space();
		}
		registeredElementBuilder.translate(elements.size() == 1 ? key : key + ".multiple", Styler::condition)
				.space()
				.cycle(cycler -> elements.stream().forEach(holder -> {
					if (nameMapper != null) {
						CompositeContainerComponent.Builder elementBuilder = CompositeContainerComponent.builder();
						Component name = Styler.name(nameMapper.apply(holder.unwrap().map(registry::getValueOrThrow, Function.identity()), elementBuilder).copy());
						cycler.component(CompositeContainerComponent.builder().textComponent(name).component(elementBuilder.build()).build());
					} else {
						cycler.textComponent(Styler.name(Component.literal(holder.unwrap().map(ResourceKey::location, registry::getKey).toString())));
					}
				}))
				.build();
		builder.component(registeredElementBuilder.build());
	}

	static <T extends Enum<T>> void addEnumListToBuilder(String key, List<T> enums, Function<T, Component> nameMapper, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		CompositeContainerComponent.Builder enumListBuilder = CompositeContainerComponent.builder();
		if (state.doPrefixSpace()) {
			enumListBuilder.space();
		}
		builder.component(enumListBuilder
				.translate(key, Styler::condition)
				.space()
				.cycle(cycler -> enums.stream()
						.map(nameMapper)
						.map(component -> (Component) Styler.name(component.copy()))
						.forEach(cycler::textComponent))
				.build());
	}

	static <T> void addTagsToBuilder(String key, List<TagPredicate<T>> tags, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		CompositeContainerComponent.Builder tagBuilder = CompositeContainerComponent.builder();
		if (state.doPrefixSpace()) {
			tagBuilder.space();
		}
		tagBuilder.translate(tags.size() == 1 ? key : key + ".multiple", Styler::condition)
				.space()
				.cycle(cycler -> tags.forEach(tag -> {
					TagKey<T> tagKey = tag.tag();
					String prefix = tag.expected() ? "" : "!";
					cycler.textComponent(Styler.name(Component.literal("%s#%s".formatted(prefix, tagKey.location().toString()))));
				}))
				.build();
		builder.component(tagBuilder.build());
	}
}
