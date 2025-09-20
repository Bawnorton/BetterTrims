package com.bawnorton.bettertrims.property.item.type;

import com.mojang.serialization.Codec;

public record DamageImmunityItemProperty() {
    public static final DamageImmunityItemProperty INSTANCE = new DamageImmunityItemProperty();
    public static final Codec<DamageImmunityItemProperty> CODEC = Codec.unit(() -> INSTANCE);
}
