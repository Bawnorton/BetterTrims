package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
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
import java.util.function.Function;

import static com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip.addMinMaxToBuilder;

public interface LocationPredicateTooltip {
    static void addToBuilder(ClientLevel level, LocationPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<LocationPredicate.PositionPredicate> position = predicate.position();
        if(position.isPresent()) {
            addPositionPredicateToBuilder(level, position.orElseThrow(), builder);
        }

        Optional<HolderSet<Biome>> biomes = predicate.biomes();
        if(biomes.isPresent()) {
            addBiomesToBuilder(level, biomes.orElseThrow(), builder);
        }

        Optional<HolderSet<Structure>> structures = predicate.structures();
        if(structures.isPresent()) {
            addStructuresToBuilder(level, structures.orElseThrow(), builder);
        }

        Optional<ResourceKey<Level>> dimension = predicate.dimension();
        if(dimension.isPresent()) {
            addDimensionToBuilder(level, dimension.orElseThrow(), builder);
        }

        Optional<Boolean> smokey = predicate.smokey();
        if(smokey.isPresent()) {
            addSmokeyToBuilder(level, smokey.orElseThrow(), builder);
        }

        Optional<LightPredicate> light = predicate.light();
        if(light.isPresent()) {
            addLightPredicateToBuilder(level, light.orElseThrow(), builder);
        }

        Optional<BlockPredicate> block = predicate.block();
        if(block.isPresent()) {
            addBlockPredicateToBuilder(level, block.orElseThrow(), builder);
        }

        Optional<FluidPredicate> fluid = predicate.fluid();
        if(fluid.isPresent()) {
            addFluidPredicateToBuilder(level, fluid.orElseThrow(), builder);
        }

        Optional<Boolean> canSeeSky = predicate.canSeeSky();
        if(canSeeSky.isPresent()) {
            addCanSeeSkyToBuilder(level, canSeeSky.orElseThrow(), builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("location.%s".formatted(key));
    }

    static void addPositionPredicateToBuilder(ClientLevel level, LocationPredicate.PositionPredicate predicate, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Doubles x = predicate.x();
        MinMaxBounds.Doubles y = predicate.y();
        MinMaxBounds.Doubles z = predicate.z();

        CompositeContainerComponent.Builder positionBuilder = CompositeContainerComponent.builder()
            .space();
        boolean useAnd = false;
        if(!x.isAny()) {
            addMinMaxToBuilder(key("position.x"), useAnd, x.min().orElse(null), x.max().orElse(null), positionBuilder);
            useAnd = true;
        }
        if(!y.isAny()) {
            addMinMaxToBuilder(key("position.y"), useAnd, y.min().orElse(null), y.max().orElse(null), positionBuilder);
            useAnd = true;
        }
        if(!z.isAny()) {
            addMinMaxToBuilder(key("position.z"), useAnd, z.min().orElse(null), z.max().orElse(null), positionBuilder);
        }
        builder.component(positionBuilder.build());
    }

    static void addBiomesToBuilder(ClientLevel level, HolderSet<Biome> biomes, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("biome"), Registries.BIOME, biomes, builder);
    }

    static void addStructuresToBuilder(ClientLevel level, HolderSet<Structure> structures, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("structure"), Registries.STRUCTURE, structures, builder);
    }

    static void addDimensionToBuilder(ClientLevel level, ResourceKey<Level> dimension, CompositeContainerComponent.Builder builder) {
        Component dimensionName = Styler.name(Component.literal(dimension.toString()));
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("dimension"), Styler::condition)
            .space()
            .textComponent(dimensionName)
            .build());
    }

    static void addSmokeyToBuilder(ClientLevel level, boolean smokey, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder().space().translate(key("smokey.%s".formatted(smokey)), Styler::value).build());
    }

    static void addLightPredicateToBuilder(ClientLevel level, LightPredicate predicate, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Ints composite = predicate.composite();
        if(composite.isAny()) {
            builder.space().translate(key("light.any"), Styler::value);
        } else {
            addMinMaxToBuilder(key("light.level"), false, composite.min().orElse(null), composite.max().orElse(null), builder);
        }
    }

    static void addBlockPredicateToBuilder(ClientLevel level, BlockPredicate predicate, CompositeContainerComponent.Builder builder) {
        BlockPredicateTooltip.addToBuilder(level, predicate, builder);
    }

    static void addFluidPredicateToBuilder(ClientLevel level, FluidPredicate predicate, CompositeContainerComponent.Builder builder) {
        FluidPredicateTooltip.addToBuilder(level, predicate, builder);
    }

    static void addCanSeeSkyToBuilder(ClientLevel level, boolean canSeeSky, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder().space().translate(key("can_see_sky.%s".formatted(canSeeSky)), Styler::value).build());
    }
}
