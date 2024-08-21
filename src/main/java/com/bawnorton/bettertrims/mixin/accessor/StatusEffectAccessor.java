package com.bawnorton.bettertrims.mixin.accessor;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StatusEffect.class)
public interface StatusEffectAccessor {
    @Invoker("<init>")
    static StatusEffect createStatusEffect(StatusEffectCategory category, int color) {
        throw new UnsupportedOperationException();
    }
}
