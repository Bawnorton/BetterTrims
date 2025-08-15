package com.bawnorton.bettertrims.ability.type;

import com.bawnorton.bettertrims.BetterTrims;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record TrimAbilityType<T extends TrimAbility>(MapCodec<T> codec) {
    public static final Registry<TrimAbilityType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(BetterTrims.rl("trim_ability_types")),
            Lifecycle.stable()
    );

    public static final TrimAbilityType<AttributeTrimAbility> ATTRIBUTE = register("attribute", new TrimAbilityType<>(AttributeTrimAbility.CODEC));
    public static final TrimAbilityType<EffectTrimAbility> EFFECT = register("effect", new TrimAbilityType<>(EffectTrimAbility.CODEC));
    public static final TrimAbilityType<WearingGoldTrimAbility> WEARING_GOLD = register("wearing_gold", new TrimAbilityType<>(WearingGoldTrimAbility.CODEC));

    private static <T extends TrimAbility> TrimAbilityType<T> register(String path, TrimAbilityType<T> type) {
        return Registry.register(REGISTRY, BetterTrims.rl(path), type);
    }
}
