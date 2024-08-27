package com.bawnorton.bettertrims.mixin.trim.leather;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityExtender {
    @ModifyReturnValue(
            method = "canFreeze",
            at = @At("RETURN")
    )
    private boolean cantFreezeInLeatherTrim(boolean original) {
        if(!original) return false;

        return !bettertrims$isWearing(TrimEffects.LEATHER);
    }
}
