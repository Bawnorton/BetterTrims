package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.registry.TrimRegistryKeys;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RegistryKeys.class)
public abstract class RegistryKeysMixin {
    @Shadow
    private static <T> RegistryKey<Registry<T>> of(String id) {
        throw new AssertionError();
    }

    static {
        TrimRegistryKeys.TRIM_EFFECTS = of("trim_effects");
    }
}
