package com.bawnorton.bettertrims.property.item;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.item.type.DamageResistantItemProperty;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record TrimItemPropertyType<T extends TrimItemProperty>(MapCodec<T> codec) {
    public static final Registry<TrimItemPropertyType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(BetterTrims.rl("trimmed_item_property_type")),
            Lifecycle.stable()
    );

    public static final TrimItemPropertyType<DamageResistantItemProperty> DAMAGE_RESISTANT = register("damage_resistant", new TrimItemPropertyType<>(DamageResistantItemProperty.CODEC));

    private static <T extends TrimItemProperty> TrimItemPropertyType<T> register(String path, TrimItemPropertyType<T> type) {
        return Registry.register(REGISTRY, BetterTrims.rl(path), type);
    }
}
