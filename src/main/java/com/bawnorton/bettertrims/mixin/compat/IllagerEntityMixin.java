package com.bawnorton.bettertrims.mixin.compat;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IllagerEntity.class)
public abstract class IllagerEntityMixin extends LivingEntityMixin {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
    protected boolean canTargetTrimmedPlayer(boolean original, LivingEntity target) {
        return original && !ArmorTrimEffects.PLATINUM.appliesTo(((LivingEntityExtender) target).betterTrims$getTrimmables());
    }
}





