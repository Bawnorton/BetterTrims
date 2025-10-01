package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.DataComponentMatchersTooltip;
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
    static void addToBuilder(ClientLevel level, ItemPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Optional<HolderSet<Item>> items = predicate.items();
        if (items.isPresent()) {
            addItemsToBuilder(level, items.orElseThrow(), state, builder);
            state = state.withUseWith(true);
        }

        DataComponentMatchers components = predicate.components();
        if (!components.isEmpty()) {
            addDataComponentMatchersToBuilder(level, components, state, builder);
        }

        MinMaxBounds.Ints count = predicate.count();
        if (!count.isAny()) {
            addCountToBuilder(level, count, state, builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("item.%s".formatted(key));
    }

    static void addItemsToBuilder(ClientLevel level, HolderSet<Item> items, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.ITEM, items, item -> item.getName(), state, builder);
    }

    static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        if (state.doPrefixSpace()) {
            builder.space();
        }
        builder.translate("bettertrims.tooltip.condition.match_tool.has.%s".formatted(state.key()), Styler::condition);
        DataComponentMatchersTooltip.addToBuilder(level, components, state, builder);
    }

    static void addCountToBuilder(ClientLevel level, MinMaxBounds.Ints count, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        CompositeContainerComponent.Builder countBuilder = CompositeContainerComponent.builder().space();
        addMinMaxToBuilder(key("count"), false, count, state, countBuilder);
        builder.component(countBuilder.build());
    }
}
