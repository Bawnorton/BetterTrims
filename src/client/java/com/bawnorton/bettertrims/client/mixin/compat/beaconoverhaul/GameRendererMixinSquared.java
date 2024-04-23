package com.bawnorton.bettertrims.client.mixin.compat.beaconoverhaul;

import com.bawnorton.bettertrims.util.mixin.annotation.ConditionalMixin;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 1500)
@ConditionalMixin(modid = "beaconoverhaul")
public abstract class GameRendererMixinSquared {
    @TargetHandler(
            mixin = "dev.sapphic.beacons.client.mixin.GameRendererMixin",
            name = "noNightVisionFlickerWhenAmbient"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), cancellable = true)
    private static void dontCrashOnFakeNightVisionEffect(CallbackInfo ci, @Local(argsOnly = true) StatusEffectInstance effect) {
        if (effect == null) ci.cancel();
    }
}
