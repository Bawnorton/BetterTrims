package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

import static com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip.addMinMaxToBuilder;

public interface LocationPredicateTooltip {
	static void addToBuilder(ClientLevel level, LocationPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<LocationPredicate.PositionPredicate> position = predicate.position();
		if (position.isPresent()) {
			addPositionPredicateToBuilder(level, position.orElseThrow(), state, builder);
		}

		Optional<HolderSet<Biome>> biomes = predicate.biomes();
		if (biomes.isPresent()) {
			addBiomesToBuilder(level, biomes.orElseThrow(), state, builder);
		}

		Optional<HolderSet<Structure>> structures = predicate.structures();
		if (structures.isPresent()) {
			addStructuresToBuilder(level, structures.orElseThrow(), state, builder);
		}

		Optional<ResourceKey<Level>> dimension = predicate.dimension();
		if (dimension.isPresent()) {
			addDimensionToBuilder(level, dimension.orElseThrow(), state, builder);
		}

		Optional<Boolean> smokey = predicate.smokey();
		if (smokey.isPresent()) {
			addSmokeyToBuilder(level, smokey.orElseThrow(), state, builder);
		}

		Optional<LightPredicate> light = predicate.light();
		if (light.isPresent()) {
			addLightPredicateToBuilder(level, light.orElseThrow(), state, builder);
		}

		Optional<BlockPredicate> block = predicate.block();
		if (block.isPresent()) {
			addBlockPredicateToBuilder(level, block.orElseThrow(), state, builder);
		}

		Optional<FluidPredicate> fluid = predicate.fluid();
		if (fluid.isPresent()) {
			addFluidPredicateToBuilder(level, fluid.orElseThrow(), state, builder);
		}

		Optional<Boolean> canSeeSky = predicate.canSeeSky();
		if (canSeeSky.isPresent()) {
			addCanSeeSkyToBuilder(level, canSeeSky.orElseThrow(), state, builder);
		}
	}

	static String key(String key) {
		return PredicateTooltip.key("location.%s".formatted(key));
	}

	static void addPositionPredicateToBuilder(ClientLevel level, LocationPredicate.PositionPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		MinMaxBounds.Doubles x = predicate.x();
		MinMaxBounds.Doubles y = predicate.y();
		MinMaxBounds.Doubles z = predicate.z();

		CompositeContainerComponent.Builder positionBuilder = CompositeContainerComponent.builder()
				.space();
		boolean useAnd = false;
		if (!x.isAny()) {
			addMinMaxToBuilder(key("position.x"), useAnd, x.min().orElse(null), x.max().orElse(null), state, positionBuilder);
			useAnd = true;
		}
		if (!y.isAny()) {
			addMinMaxToBuilder(key("position.y"), useAnd, y.min().orElse(null), y.max().orElse(null), state, positionBuilder);
			useAnd = true;
		}
		if (!z.isAny()) {
			addMinMaxToBuilder(key("position.z"), useAnd, z.min().orElse(null), z.max().orElse(null), state, positionBuilder);
		}
		builder.component(positionBuilder.build());
	}

	static void addBiomesToBuilder(ClientLevel level, HolderSet<Biome> biomes, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PredicateTooltip.addRegisteredElementsToBuilder(level, key("biome"), Registries.BIOME, biomes, state, builder);
	}

	static void addStructuresToBuilder(ClientLevel level, HolderSet<Structure> structures, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PredicateTooltip.addRegisteredElementsToBuilder(level, key("structure"), Registries.STRUCTURE, structures, state, builder);
	}

	static void addDimensionToBuilder(ClientLevel level, ResourceKey<Level> dimension, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Component dimensionName = Styler.name(Component.literal(dimension.toString()));
		builder.component(CompositeContainerComponent.builder()
				.space()
				.translate(key("dimension"), Styler::condition)
				.space()
				.textComponent(dimensionName)
				.build());
	}

	static void addSmokeyToBuilder(ClientLevel level, boolean smokey, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		builder.component(CompositeContainerComponent.builder().space().translate(key("smokey.%s".formatted(smokey)), Styler::value).build());
	}

	static void addLightPredicateToBuilder(ClientLevel level, LightPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		MinMaxBounds.Ints composite = predicate.composite();
		if (composite.isAny()) {
			builder.space().translate(key("light.any"), Styler::value);
		} else {
			addMinMaxToBuilder(key("light.level"), false, composite.min().orElse(null), composite.max().orElse(null), state, builder);
		}
	}

	static void addBlockPredicateToBuilder(ClientLevel level, BlockPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		BlockPredicateTooltip.addToBuilder(level, predicate, state, builder);
	}

	static void addFluidPredicateToBuilder(ClientLevel level, FluidPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		FluidPredicateTooltip.addToBuilder(level, predicate, state, builder);
	}

	static void addCanSeeSkyToBuilder(ClientLevel level, boolean canSeeSky, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		builder.component(CompositeContainerComponent.builder().space().translate(key("can_see_sky.%s".formatted(canSeeSky)), Styler::value).build());
	}
}
