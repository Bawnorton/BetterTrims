package com.bawnorton.bettertrims.mixin.trim.iron;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;updateLeaningPitch()V"
            )
    )
    private void applyIronTrim(CallbackInfo ci) {
        if(!TrimEffects.IRON.matches(this)) return;

        TrimEffects.IRON.getApplicator().apply(TrimContext.empty(), (LivingEntity) (Object) this);
    }
}
