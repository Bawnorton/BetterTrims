package com.bawnorton.bettertrims.mixin.attributes.celestial;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttackGoal.class)
public abstract class AttackGoalMixin {
    @Shadow @Final private MobEntity mob;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=20"
            )
    )
    private int applyCelestialToAttackCooldown(int original) {
        return ((LivingEntityExtender) mob).bettertrims$applyCelestialToAttackCooldown(original);
    }
}
