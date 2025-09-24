package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import java.util.Optional;

import static com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip.addMinMaxToBuilder;

public interface ItemPredicateTooltip {
    static void addToBuilder(ClientLevel level, ItemPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<HolderSet<Item>> items = predicate.items();
        if(items.isPresent()) {
            addItemsToBuilder(level, items.orElseThrow(), builder);
        }

        DataComponentMatchers components = predicate.components();
        if(!components.isEmpty()) {
            addDataComponentMatchersToBuilder(level, components, builder);
        }

        MinMaxBounds.Ints count = predicate.count();
        if(!count.isAny()) {
            addCountToBuilder(level, count, builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("item.%s".formatted(key));
    }

    static void addItemsToBuilder(ClientLevel level, HolderSet<Item> items, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.ITEM, items, item -> item.getName(), builder);
    }

    static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, CompositeContainerComponent.Builder builder) {
        DataComponentMatchersTooltip.addToBuilder(level, components, builder);
    }

    static void addCountToBuilder(ClientLevel level, MinMaxBounds.Ints count, CompositeContainerComponent.Builder builder) {
        CompositeContainerComponent.Builder countBuilder = CompositeContainerComponent.builder().space();
        addMinMaxToBuilder(key("count"), false, count, countBuilder);
        builder.component(countBuilder.build());
    }
}
