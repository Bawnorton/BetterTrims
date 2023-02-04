package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.config.Config;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.RandomHelper;
import com.bawnorton.util.Wrapper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {

    @Shadow @Final private StatusEffect type;
    @Shadow private int duration;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I", shift = At.Shift.AFTER))
    private void modifyDuration(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        Wrapper<Float> chance = Wrapper.of(0f);
        ArmorTrimEffects.AMETHYST.apply(entity.getArmorItems(), stack -> chance.set(chance.get() + BetterTrims.CONFIG.amethystEffectChance));
        if (RandomHelper.nextFloat() < chance.get()) duration += type.isBeneficial() ? 1 : -1;
    }
}
