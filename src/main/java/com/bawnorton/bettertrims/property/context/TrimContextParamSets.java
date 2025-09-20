package com.bawnorton.bettertrims.property.context;

import com.bawnorton.bettertrims.BetterTrims;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.function.Consumer;

public final class TrimContextParamSets {
    private static final BiMap<ResourceLocation, ContextKeySet> REGISTRY = HashBiMap.create();

    public static final ContextKeySet TRIM_DAMAGE = register(
        "trim_damage",
        builder -> builder.required(LootContextParams.THIS_ENTITY)
            .required(TrimContextParams.ITEMS)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.DAMAGE_SOURCE)
            .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
            .optional(LootContextParams.ATTACKING_ENTITY)
            .optional(LootContextParams.TOOL) // damaging tool
    );

    public static final ContextKeySet HIT_BLOCK_WITH_HELD_ITEM = register(
        "hit_block_with_held_item",
        builder -> builder.required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(TrimContextParams.ITEMS)
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.TOOL)
    );

    public static final ContextKeySet TRIM_EQUIPMENT = register(
        "trim_equipment",
        builder -> builder.required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(TrimContextParams.ITEMS)
            .optional(LootContextParams.TOOL)
    );

    public static final ContextKeySet TRIM_ITEM_DAMAGE = register(
        "trim_item_damage",
        builder -> builder.required(LootContextParams.TOOL)
            .required(LootContextParams.DAMAGE_SOURCE)
            .optional(LootContextParams.THIS_ENTITY)
            .optional(LootContextParams.ORIGIN)
            .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
            .optional(LootContextParams.ATTACKING_ENTITY)
    );

    public static final ContextKeySet TRIM_ENTITY = register(
        "trim_entity",
        builder -> builder.required(LootContextParams.THIS_ENTITY)
            .required(TrimContextParams.ITEMS)
            .required(LootContextParams.ORIGIN)
    );

    private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> ctor) {
        ContextKeySet.Builder builder = new ContextKeySet.Builder();
        ctor.accept(builder);
        ContextKeySet contextKeySet = builder.build();
        ResourceLocation resourceLocation = BetterTrims.rl(name);
        if (REGISTRY.put(resourceLocation, contextKeySet) != null) {
            throw new IllegalStateException("Trim parameter set " + resourceLocation + " is already registered");
        } else {
            return contextKeySet;
        }
    }
}
