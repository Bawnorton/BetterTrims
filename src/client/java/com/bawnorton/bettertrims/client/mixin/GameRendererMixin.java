package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.config.ClientConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @WrapOperation(method = "getNightVisionStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isDurationBelow(I)Z"))
    private static boolean letsNotCrashThanks(StatusEffectInstance instance, int duration, Operation<Boolean> original) {
        if (instance == null) return false;
        return original.call(instance, duration);
    }

    @ModifyReturnValue(method = "getNightVisionStrength", at = @At("RETURN"))
    private static float improveNightVisionWhenWearingSilver(float original, LivingEntity entity, float tickDelta) {
        NumberWrapper increase = NumberWrapper.zero();
        if (entity instanceof LivingEntityExtender extender && extender.betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(extender.betterTrims$getTrimmables(), () -> increase.increment(ClientConfigManager.getConfig().silverEffects.improveVision));
        }
        return Math.min(original + increase.getFloat(), 1.0F);
    }
}
