package com.bawnorton.bettertrims.client.mixin.compat.iris;

import com.bawnorton.bettertrims.client.config.ClientConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.mixin.IrisConditionChecker;
import com.bawnorton.bettertrims.util.mixin.annotation.AdvancedConditionalMixin;
import com.bawnorton.bettertrims.util.mixin.annotation.VersionPredicate;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = GameRenderer.class, priority = 1500)
@AdvancedConditionalMixin(checker = IrisConditionChecker.class, version = @VersionPredicate(min = "1.7"))
public abstract class Iris17PlusGameRendererMixinSquared {
    @TargetHandler(
            mixin = "net.irisshaders.iris.mixin.MixinGameRenderer_NightVisionCompat",
            name = "iris$safecheckNightvisionStrength"
    )
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "org/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable.setReturnValue (Ljava/lang/Object;)V"))
    private static Object applySilverTrimNightVision(Object original, @Local(argsOnly = true) LivingEntity entity) {
        float originalFloat = (Float) original;
        NumberWrapper increase = NumberWrapper.zero();
        if (entity instanceof LivingEntityExtender extender && extender.betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(extender.betterTrims$getTrimmables(), () -> increase.increment(ClientConfigManager.getConfig().silverEffects.improveVision));
        }
        return Math.min(originalFloat + increase.getFloat(), 1.0F);
    }
}
