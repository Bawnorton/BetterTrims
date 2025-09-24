package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import java.util.Optional;

public interface FluidPredicateTooltip {
    static void addToBuilder(ClientLevel level, FluidPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<HolderSet<Fluid>> fluids = predicate.fluids();
        if(fluids.isPresent()) {
            addFluidsToBuilder(level, fluids.orElseThrow(), builder);
        }

        Optional<StatePropertiesPredicate> properties = predicate.properties();
        if(properties.isPresent()) {
            addPropertiesToBuilder(level, properties.orElseThrow(), builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("fluid.%s".formatted(key));
    }

    static void addFluidsToBuilder(ClientLevel level, HolderSet<Fluid> fluids, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.FLUID, fluids, fluid -> fluid.defaultFluidState().createLegacyBlock().getBlock().getName(), builder);
    }

    static void addPropertiesToBuilder(ClientLevel level, StatePropertiesPredicate properties, CompositeContainerComponent.Builder builder) {
        BlockPredicateTooltip.addStatePropertiesPredicateToBuilder(level, properties, builder);
    }
}
