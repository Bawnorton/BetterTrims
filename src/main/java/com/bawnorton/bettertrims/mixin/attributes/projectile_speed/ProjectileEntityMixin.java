package com.bawnorton.bettertrims.mixin.attributes.projectile_speed;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
    @ModifyVariable(
            method = "setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 3
    )
    private float applyProjectileSpeed(float original, @Local(argsOnly = true) Entity shooter) {
        if (!(shooter instanceof LivingEntity livingEntity)) return original;

        float additionalSpeed = (float) (livingEntity.getAttributeValue(TrimEntityAttributes.PROJECTILE_SPEED) - 1);
        return original + original * additionalSpeed;
    }
}
