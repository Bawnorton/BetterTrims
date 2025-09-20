package com.bawnorton.bettertrims.property.count;

import com.bawnorton.bettertrims.BetterTrims;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record CountBasedValueType<T extends CountBasedValue>(MapCodec<T> codec) {
    public static final Registry<CountBasedValueType<?>> REGISTRY = new MappedRegistry<>(
        ResourceKey.createRegistryKey(BetterTrims.rl("count_based_value_types")),
        Lifecycle.stable()
    );
}
