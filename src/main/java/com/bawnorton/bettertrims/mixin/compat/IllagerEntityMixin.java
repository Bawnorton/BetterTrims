package com.bawnorton.bettertrims.mixin.compat;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IllagerEntity.class)
public abstract class IllagerEntityMixin extends LivingEntityMixin {
    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
    protected boolean canTargetTrimmedPlayer(boolean original, LivingEntity target) {
        if (!original) return false;
        if (!ConfigManager.getConfig().platinumEffects.illagersIgnore) return true;

        NumberWrapper trimCount = NumberWrapper.zero();
        ArmorTrimEffects.PLATINUM.apply(((LivingEntityExtender) target).betterTrims$getTrimmables(), () -> trimCount.increment(1));
        if(target.equals(getAttacker())) return true;

        boolean satisfiesTrimCount = trimCount.getInt() >= ConfigManager.getConfig().platinumEffects.piecesForIllagersIgnore;
        return !satisfiesTrimCount;
    }
}





