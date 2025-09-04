package com.bawnorton.bettertrims.property.item;

import com.bawnorton.bettertrims.property.Matcher;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

public record TrimItemPropertyRunner(TrimItemProperty property, Matcher matcher) {
    public static final Codec<TrimItemPropertyRunner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TrimItemProperty.CODEC.fieldOf("property").forGetter(TrimItemPropertyRunner::property),
            Matcher.COUNTLESS_CODEC.fieldOf("applies_to").forGetter(TrimItemPropertyRunner::matcher)
    ).apply(instance, TrimItemPropertyRunner::new));

    public boolean isInvulnerableTo(ItemStack instance, DamageSource damageSource) {
        if (matcher.matches(instance)) {
            return property.isInvulnerableTo(instance, damageSource);
        }
        return false;
    }
}
