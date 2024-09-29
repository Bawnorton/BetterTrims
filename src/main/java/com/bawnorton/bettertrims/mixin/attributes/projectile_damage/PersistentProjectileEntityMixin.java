package com.bawnorton.bettertrims.mixin.attributes.projectile_damage;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
            method = "onEntityHit",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;damage:D"
            )
    )
    private double applyProjectileDamage(double original) {
        if (!(getOwner() instanceof LivingEntity livingEntity)) return original;

        double additionalDamage = livingEntity.getAttributeValue(TrimEntityAttributes.PROJECTILE_DAMAGE);
        return original + additionalDamage;
    }
}
