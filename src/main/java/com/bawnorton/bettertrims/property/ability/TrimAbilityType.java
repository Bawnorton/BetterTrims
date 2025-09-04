package com.bawnorton.bettertrims.property.ability;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.ability.type.AttributeTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.DamageResistantAbility;
import com.bawnorton.bettertrims.property.ability.type.EffectTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.ElectrifyingTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.ExperienceGainTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.WearingGoldTrimAbility;
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
    public static final TrimAbilityType<ExperienceGainTrimAbility> EXPERIENCE_GAIN = register("experience_gain", new TrimAbilityType<>(ExperienceGainTrimAbility.CODEC));
    public static final TrimAbilityType<DamageResistantAbility> DAMAGE_RESISTANT = register("damage_resistant", new TrimAbilityType<>(DamageResistantAbility.CODEC));
    public static final TrimAbilityType<ElectrifyingTrimAbility> ELECTRIFYING = register("electrifying", new TrimAbilityType<>(ElectrifyingTrimAbility.CODEC));

    private static <T extends TrimAbility> TrimAbilityType<T> register(String path, TrimAbilityType<T> type) {
        return Registry.register(REGISTRY, BetterTrims.rl(path), type);
    }
}
