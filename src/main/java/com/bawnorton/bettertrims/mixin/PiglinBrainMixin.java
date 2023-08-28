package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "wearsGoldArmor", at = @At("RETURN"))
    private static boolean checkPlayerTrims(boolean original, LivingEntity entity) {
        boolean hasGoldTrim = ArmorTrimEffects.GOLD.appliesTo(((EntityExtender) entity).betterTrims$getTrimmables());
        boolean hasNetherBrickTrim = ArmorTrimEffects.NETHER_BRICK.appliesTo(((EntityExtender) entity).betterTrims$getTrimmables());
        return (original || hasGoldTrim) && !hasNetherBrickTrim;
    }
}
