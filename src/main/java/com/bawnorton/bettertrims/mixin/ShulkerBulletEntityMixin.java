package com.bawnorton.bettertrims.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletEntityMixin extends EntityMixin {
    @SuppressWarnings("unused")
    @WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean shouldHit(Entity instance, DamageSource source, float amount, Operation<Boolean> orginal) {
        if (didDodgeAttack(instance)) return false;

        return orginal.call(instance, source, amount);
    }
}
