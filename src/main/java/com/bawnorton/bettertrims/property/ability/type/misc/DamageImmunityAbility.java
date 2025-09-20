package com.bawnorton.bettertrims.property.ability.type.misc;

import com.mojang.serialization.Codec;

public record DamageImmunityAbility() {
    public static final DamageImmunityAbility INSTANCE = new DamageImmunityAbility();
    public static final Codec<DamageImmunityAbility> CODEC = Codec.unit(() -> INSTANCE);
}
