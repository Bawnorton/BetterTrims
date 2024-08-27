package com.bawnorton.bettertrims.mixin.trim.gold;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @ModifyReturnValue(
            method = "wearsGoldArmor",
            at = @At("RETURN")
    )
    private static boolean orWearsGoldTrim(boolean original, LivingEntity entity) {
        if (original) return true;

        return ((LivingEntityExtender) entity).bettertrims$isWearing(TrimEffects.GOLD);
    }
}