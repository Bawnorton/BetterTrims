package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Shadow @Final private static Logger LOGGER;

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int modifyExperience(int experience) {
        if (experience <= 0) return experience;
        NumberWrapper increase = NumberWrapper.of(1F);
        ArmorTrimEffects.QUARTZ.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().quartzExperienceBonus));
        return (int) (experience * increase.getFloat());
    }

    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    private float modifyMovementSpeed(float original) {
        NumberWrapper increase = NumberWrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.movementSpeed));
        }
        return original * increase.getFloat();
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float modifyAttributeModifiers(float original) {
        NumberWrapper increase = NumberWrapper.of(original);
        if(betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.attackDamage));
        }
        return increase.getFloat();
    }

    @ModifyExpressionValue(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double modifyAttackCooldown(double original) {
        NumberWrapper increase = NumberWrapper.of(original);
        if(betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.attackSpeed));
        }
        return increase.getFloat();
    }
}
