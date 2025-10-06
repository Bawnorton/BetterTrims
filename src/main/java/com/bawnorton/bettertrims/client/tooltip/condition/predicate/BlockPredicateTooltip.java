package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

//? if >=1.21.8 {
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.DataComponentMatchersTooltip;
import net.minecraft.advancements.critereon.DataComponentMatchers;
//?}

public interface BlockPredicateTooltip {
	static void addToBuilder(ClientLevel level, BlockPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<HolderSet<Block>> blocks = predicate.blocks();
		if (blocks.isPresent()) {
			addBlocksToBuilder(level, blocks.orElseThrow(), state, builder);
		}

		//? if >=1.21.8 {
    DataComponentMatchers components = predicate.components();
    if (!components.isEmpty()) {
        addDataComponentMatchersToBuilder(level, components, state, builder);
    }
    //?}

		Optional<NbtPredicate> nbt = predicate.nbt();
		if (nbt.isPresent()) {
			addNbtPredicateToBuilder(level, nbt.orElseThrow(), state, builder);
		}

		Optional<StatePropertiesPredicate> properties = predicate.properties();
		if (properties.isPresent()) {
			addStatePropertiesPredicateToBuilder(level, properties.orElseThrow(), state, builder);
		}
	}

	static String key(String key) {
		return PredicateTooltip.key("block.%s".formatted(key));
	}

	static void addBlocksToBuilder(ClientLevel level, HolderSet<Block> blocks, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.BLOCK, blocks, Block::getName, state, builder);
	}

	//? if >=1.21.8 {
  static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
      DataComponentMatchersTooltip.addToBuilder(level, components, state, builder);
  }
  //?}

	static void addNbtPredicateToBuilder(ClientLevel level, NbtPredicate nbt, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		EntityPredicateTooltip.addNbtPredicateToBuilder(level, key("nbt"), nbt, state, builder);
	}

	static void addStatePropertiesPredicateToBuilder(ClientLevel level, StatePropertiesPredicate properties, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		List<StatePropertiesPredicate.PropertyMatcher> matchers = properties.properties();
		if (matchers.isEmpty()) {
			builder.component(CompositeContainerComponent.builder()
					.space()
					.translate(key("state_properties.any"), Styler::value)
					.build());
		} else {
			builder.component(CompositeContainerComponent.builder()
					.space()
					.cycle(propertyCycle -> {
						for (StatePropertiesPredicate.PropertyMatcher matcher : matchers) {
							CompositeContainerComponent.Builder propertyMatcherBuilder = CompositeContainerComponent.builder();
							Component matcherName = Styler.property(Component.literal(StringUtils.capitalize(matcher.name())));
							switch (matcher.valueMatcher()) {
								case StatePropertiesPredicate.ExactMatcher exact -> propertyMatcherBuilder.translate(
										key("state_property.exact"),
										Styler::condition,
										matcherName,
										Styler.value(Component.literal(StringUtils.capitalize(exact.value())))
								);
								case StatePropertiesPredicate.RangedMatcher ranged -> {
									String min = ranged.minValue().orElse(null);
									String max = ranged.maxValue().orElse(null);

									if (min == null && max == null) {
										propertyMatcherBuilder.translate(
												key("state_property.any"),
												Styler::condition,
												matcherName
										);
									} else if (min != null && max != null) {
										propertyMatcherBuilder.translate(
												key("state_property.between"),
												Styler::condition,
												matcherName,
												Styler.number(Component.literal(min)),
												Styler.number(Component.literal(max))
										);
									} else if (min != null) {
										propertyMatcherBuilder.translate(
												key("state_property.at_least"),
												Styler::condition,
												matcherName,
												Styler.number(Component.literal(min))
										);
									} else {
										propertyMatcherBuilder.translate(
												key("state_property.at_most"),
												Styler::condition,
												matcherName,
												Styler.number(Component.literal(max))
										);
									}
								}
								default -> propertyMatcherBuilder.translate(key("state_property.any"), Styler::condition, matcherName);
							}
							propertyCycle.component(propertyMatcherBuilder.build());
						}
					})
					.build());
		}
	}
}
