package com.bawnorton.bettertrims.property.item.type;

import com.bawnorton.bettertrims.property.element.TrimElement;
import com.mojang.serialization.Codec;

public record DamageImmunityItemProperty() implements TrimElement {
	public static final DamageImmunityItemProperty INSTANCE = new DamageImmunityItemProperty();
	public static final Codec<DamageImmunityItemProperty> CODEC = Codec.unit(() -> INSTANCE);
}
