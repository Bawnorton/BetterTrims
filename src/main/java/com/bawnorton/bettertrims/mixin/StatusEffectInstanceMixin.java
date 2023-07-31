package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.RandomHelper;
import com.bawnorton.bettertrims.util.Wrapper;
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
public abstract class StatusEffectInstanceMixin {

    @Shadow
    @Final
    private StatusEffect type;
    @Shadow
    private int duration;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I", shift = At.Shift.AFTER))
    private void modifyDuration(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        Wrapper<Float> chance = Wrapper.of(0f);
        ArmorTrimEffects.AMETHYST.apply(((EntityExtender) entity).betterTrims$getTrimmables(), stack -> chance.set(chance.get() + Config.getInstance().amethystEffectChance));
        if (RandomHelper.nextFloat() < chance.get()) duration += type.isBeneficial() ? 1 : -1;
    }
}
