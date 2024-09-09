package com.bawnorton.bettertrims.mixin.attributes.share_effect_radius;

import com.bawnorton.bettertrims.extend.AreaEffectCloudEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.google.common.base.Objects;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.List;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin implements AreaEffectCloudEntityExtender {
    @Shadow protected abstract void updateColor();

    @Unique
    private LivingEntity bettertrims$trimmedOwner;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isAffectedBySplashPotions()Z"
            )
    )
    private boolean dontAffectTrimmedOwner(LivingEntity instance, Operation<Boolean> original) {
        if(!original.call(instance)) return false;

        return !Objects.equal(bettertrims$trimmedOwner, instance);
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"
            )
    )
    private List<LivingEntity> triggerShareCriteria(List<LivingEntity> original) {
        if(original.size() - 1 >= 5 && bettertrims$trimmedOwner instanceof ServerPlayerEntity player) {
            TrimCriteria.SHARED_EFFECT.trigger(player);
        }
        return original;
    }

    @Override
    public void bettertrims$setTrimmedOwner(LivingEntity trimmedOwner) {
        bettertrims$trimmedOwner = trimmedOwner;
    }

    @Override
    public void bettertrims$updateColor() {
        updateColor();
    }
}
