package com.bawnorton.bettertrims.property.item;

import com.mojang.serialization.Codec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

public interface TrimItemProperty {
    Codec<TrimItemProperty> CODEC = TrimItemPropertyType.REGISTRY.byNameCodec().dispatch(TrimItemProperty::getType, TrimItemPropertyType::codec);

    TrimItemPropertyType<? extends TrimItemProperty> getType();

    default void onTrimAdded(ItemStack stack) {
    }

    default void onTrimRemoved(ItemStack stack) {
    }

    default boolean isInvulnerableTo(ItemStack stack, DamageSource source) {
        return false;
    }
}
