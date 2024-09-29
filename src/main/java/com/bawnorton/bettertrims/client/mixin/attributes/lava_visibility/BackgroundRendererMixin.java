package com.bawnorton.bettertrims.client.mixin.attributes.lava_visibility;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyExpressionValue(
            method = "applyFog",
            at = {
                    @At(
                            value = "CONSTANT",
                            args = "floatValue=0.25",
                            ordinal = 0
                    ),
                    @At(
                            value = "CONSTANT",
                            args = "floatValue=0.0",
                            ordinal = 0
                    )
            }
    )
    private static float applyLavaVisibilityToFogStart(float original, @Local Entity entity) {
        if(TrimEntityAttributes.LAVA_VISIBILITY.isUsingAlias()) return original;
        if(!(entity instanceof LivingEntity livingEntity)) return original;

        float visibility = (float) livingEntity.getAttributeValue(TrimEntityAttributes.LAVA_VISIBILITY.get());
        return MathHelper.approximatelyEquals(original, 0) ? -visibility : original * (1 - visibility);

    }

    @ModifyExpressionValue(
            method = "applyFog",
            at = {
                    @At(
                            value = "CONSTANT",
                            //? if >=1.21 {
                            args = "floatValue=5.0",
                            //?} else {
                            /*args = "floatValue=3.0",
                            *///?}
                            ordinal = 0
                    ),
                    @At(
                            value = "CONSTANT",
                            args = "floatValue=1.0",
                            ordinal = 0
                    )
            }
    )
    private static float applyLavaVisibilityToFogEnd(float original, @Local Entity entity) {
        if(TrimEntityAttributes.LAVA_VISIBILITY.isUsingAlias()) return original;
        if(!(entity instanceof LivingEntity livingEntity)) return original;

        float visibility = (float) livingEntity.getAttributeValue(TrimEntityAttributes.LAVA_VISIBILITY.get());
        return original * (1 + visibility);

    }
}
