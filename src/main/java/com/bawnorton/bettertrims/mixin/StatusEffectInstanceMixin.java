package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.extend.StatusEffectInstanceExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.RandomHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceExtender {
    @Shadow private int duration;

    @Shadow @Final private StatusEffect type;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I", shift = At.Shift.AFTER))
    private void applyTrimDurationBuff(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.AMETHYST.apply(((LivingEntityExtender) entity).betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().amethystPotionDurationModifyChance));
        if (RandomHelper.nextFloat() < chance.getFloat()) duration += type.isBeneficial() ? 1 : -1;
    }
}
