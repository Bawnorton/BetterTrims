package com.bawnorton.bettertrims.mixin.attributes.warriors_of_old;

import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin {
    @ModifyExpressionValue(
            method = "onDeath",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
            )
    )
    private boolean dontSendMessageOnAncientSkeletonDeath(boolean original) {
        if((Object) this instanceof AncientSkeletonEntity) return false;

        return original;
    }
}
