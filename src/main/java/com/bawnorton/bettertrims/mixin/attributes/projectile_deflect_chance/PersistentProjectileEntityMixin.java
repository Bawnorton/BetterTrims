package com.bawnorton.bettertrims.mixin.attributes.projectile_deflect_chance;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin {
    public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setFireTicks(I)V"
            )
    )
    private void applyProjectileDeflection(Entity instance, int fireTicks, Operation<Void> original, @Cancellable CallbackInfo ci) {
        if (bettertrims$getDeflected()) {
            bettertrims$setDeflected(false);
            setVelocity(this.getVelocity().multiply(-0.5));
            ci.cancel();
        } else {
            original.call(instance, fireTicks);
        }
    }
}
