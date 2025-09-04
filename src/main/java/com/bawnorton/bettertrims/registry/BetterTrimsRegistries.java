package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.CountBasedValueTypes;
import com.bawnorton.bettertrims.property.TrimProperty;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BetterTrimsRegistries {
    public static final ResourceKey<Registry<TrimProperty>> TRIM_PROPERTIES = createRegistryKey("trim_properties");

    static {
        CountBasedValueTypes.init();
    }

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(BetterTrims.rl(name));
    }
}
