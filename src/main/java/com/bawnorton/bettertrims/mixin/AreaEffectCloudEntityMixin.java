package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.extend.AreaEffectCloudEntityExtender;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin implements AreaEffectCloudEntityExtender {
    @Unique
    private LivingEntity trimOwner;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAffectedBySplashPotions()Z"))
    private boolean dontAffectOwnerIfTrimmed(LivingEntity instance, Operation<Boolean> original) {
        if (!original.call(instance)) return false;

        return trimOwner != instance;
    }

    @Unique
    public void betterTrims$setTrimOwner(LivingEntity trimOwner) {
        this.trimOwner = trimOwner;
    }
}
