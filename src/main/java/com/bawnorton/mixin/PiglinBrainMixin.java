package com.bawnorton.mixin;

import com.bawnorton.effect.ArmorTrimEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @Inject(method = "wearsGoldArmor", at = @At("RETURN"), cancellable = true)
    private static void wearsGoldTrim(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        ArmorTrimEffects.GOLD.apply(entity.getArmorItems(), stack -> cir.setReturnValue(true));
    }
}
