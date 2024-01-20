package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int applyTrimExperienceIncrease(int experience) {
        if (experience <= 0) return experience;
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.QUARTZ.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().quartzExperienceBonus));
        return (int) (experience * increase.getFloat());
    }

    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    private float applyTrimSpeedIncrease(float original) {
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
        }
        return original * increase.getFloat();
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float applyTrimDamageIncrease(float original) {
        NumberWrapper increase = NumberWrapper.of(original);
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.attackDamage));
        }
        return increase.getFloat();
    }

    @ModifyExpressionValue(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double applyTrimAttackSpeedIncrease(double original) {
        NumberWrapper increase = NumberWrapper.of(original);
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.attackSpeed));
        }
        return increase.getFloat();
    }

    @ModifyExpressionValue(method = "method_30263", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getStepHeight()F"))
    private float adjustStepHeightForSneaking(float original) {
        NumberWrapper decrease = NumberWrapper.zero();
        ArmorTrimEffects.LEATHER.apply(betterTrims$getTrimmables(), () -> decrease.increment(ConfigManager.getConfig().leatherStepHeightIncrease));
        return original - decrease.getFloat();
    }
}
