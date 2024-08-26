package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.effect.TrimEffect;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import com.bawnorton.bettertrims.registry.TrimRegistryKeys;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Registries.class)
public abstract class RegistriesMixin {
    @Shadow
    private static <T> Registry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Registries.Initializer<T> initializer) {
        throw new AssertionError();
    }

    static {
        TrimRegistries.TRIM_EFFECTS = (MutableRegistry<TrimEffect>) createIntrusive(TrimRegistryKeys.TRIM_EFFECTS, registry -> TrimEffects.REDSTONE);
    }
}