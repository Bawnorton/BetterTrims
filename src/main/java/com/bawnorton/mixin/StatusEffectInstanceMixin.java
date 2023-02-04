package com.bawnorton.mixin;

import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.RandomHelper;
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
        float chance = 0f;
        for(ItemStack item: entity.getArmorItems()) {
            if (ArmorTrimEffects.AMETHYST.apply(item)) chance += 0.0625f;
        }
        if (RandomHelper.nextFloat() < chance) {
            duration += type.isBeneficial() ? 1 : -1;
        }
    }
}
