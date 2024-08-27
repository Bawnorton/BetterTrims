package com.bawnorton.bettertrims.mixin.attributes.projectile_dodge_chance;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @WrapOperation(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setFireTicks(I)V"
            )
    )
    private void applyProjectileDodgeChance(Entity instance, int fireTicks, Operation<Void> original, @Cancellable CallbackInfo ci) {
        if (instance instanceof LivingEntityExtender livingEntity && livingEntity.bettertrims$didAvoidDamage()) {
            ci.cancel();
        } else {
            original.call(instance, fireTicks);
        }
    }
}
