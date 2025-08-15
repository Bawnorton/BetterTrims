package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.ability.TrimAbilityContainer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BetterTrimsRegistries {
    public static final ResourceKey<Registry<TrimAbilityContainer>> TRIM_ABILITY = createRegistryKey("trim_ability");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(BetterTrims.rl(name));
    }
}
