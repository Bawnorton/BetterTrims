package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.mob.GuardianEntity.GuardianTargetPredicate")
public abstract class GuardianTargetPredicateMixin {
    @ModifyReturnValue(method = "test", at = @At("RETURN"))
    private boolean checkIfPlayerHasPrismarineTrim(boolean original, @Nullable LivingEntity entity) {
        if(entity instanceof EntityExtender extender) {
            return original && !(ArmorTrimEffects.PRISMARINE_SHARD.appliesTo(extender.betterTrims$getTrimmables()));
        }
        return original;
    }
}
