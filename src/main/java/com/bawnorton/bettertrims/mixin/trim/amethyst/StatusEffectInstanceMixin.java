package com.bawnorton.bettertrims.mixin.trim.amethyst;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {
    @Shadow @Final private RegistryEntry<StatusEffect> type;
    @Unique
    private static final ThreadLocal<LivingEntity> bettertrims$ENTITY_CAPTURE = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<StatusEffect> bettertrims$STATUS_EFFECT_CAPTURE = new ThreadLocal<>();

    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I"
            )
    )
    private void captureData(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        bettertrims$ENTITY_CAPTURE.set(entity);
        bettertrims$STATUS_EFFECT_CAPTURE.set(type.value());
    }

    @ModifyExpressionValue(
            method = "method_48560",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=1"
            )
    )
    private static int applyAmethystTrimToDuration(int original) {
        LivingEntity entity = bettertrims$ENTITY_CAPTURE.get();
        if(!TrimEffects.AMETHYST.matches(entity)) return original;

        StatusEffect effect = bettertrims$STATUS_EFFECT_CAPTURE.get();
        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.STATUS_EFFECT, effect);
        return TrimEffects.AMETHYST.getApplicator().apply(new TrimContext(entity, builder));
    }
}
