package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.RandomHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;
    @Unique
    private int stopwatch = 0;

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float amount);

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float applyTrimSwimSpeedIncrease(float speed) {
        NumberWrapper increase = NumberWrapper.zero();
        ArmorTrimEffects.COPPER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().copperSwimSpeedIncrease));
        return speed + increase.getFloat();
    }

    @ModifyReturnValue(method = "getMovementSpeed()F", at = @At("RETURN"))
    private float applyTrimSpeedIncrease(float original) {
        if (((LivingEntity) (Object) this) instanceof AbstractHorseEntity horseEntity) {
            if (horseEntity.getControllingPassenger() instanceof PlayerEntity player) {
                NumberWrapper increase = NumberWrapper.one();
                ArmorTrimEffects.REDSTONE.apply(((EntityExtender) player).betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
                if (betterTrims$shouldSilverApply()) {
                    ArmorTrimEffects.SILVER.apply(((EntityExtender) player).betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
                }
                return original * increase.getFloat();
            }
        }
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
        }
        return original * increase.getFloat();
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 2), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z")))
    private Vec3d applyTrimSpeedIncrease(Vec3d original) {
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
        }
        return original.multiply(increase.getFloat(), 1, increase.getFloat());
    }

    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float applyTrimDamageReduction(float damage, float armor, float armorToughness, Operation<Float> original) {
        float orignal = original.call(damage, armor, armorToughness);
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.DIAMOND.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().diamondDamageReduction));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().silverEffects.damageReduction));
        }
        return decrease.getFloat() * orignal;
    }

    @ModifyReturnValue(method = "getJumpVelocity", at = @At("RETURN"))
    private float applyTrimJumpHeight(float original) {
        if (betterTrims$shouldSilverApply()) {
            NumberWrapper increase = NumberWrapper.zero();
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.jumpHeight));
            return original + increase.getFloat();
        }
        return original;
    }

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"))
    private Entity applyFireChargeTrim(Entity original) {
        if (!ArmorTrimEffects.FIRE_CHARGE.appliesTo(betterTrims$getTrimmables())) return original;

        NumberWrapper duration = NumberWrapper.zero();
        ArmorTrimEffects.FIRE_CHARGE.apply(betterTrims$getTrimmables(), () -> duration.increment(ConfigManager.getConfig().fireChargeFireDuration));
        if (duration.getInt() > 0) {
            original.setOnFireFor(duration.getInt());
        }
        return original;
    }

    @ModifyReturnValue(method = "computeFallDamage", at = @At("RETURN"))
    private int applyTrimFallDamageReduction(int original) {
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().slimeBallEffects.fallDamageReduction));
        return (int) (original * decrease.getFloat());
    }

    @ModifyExpressionValue(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double applyTrimKnockbackDecrease(double original) {
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().slimeBallEffects.knockbackIncrease));
        return original * decrease.getFloat();
    }

    @Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", remap = false))
    private void manageAreaEffectCloud(CallbackInfo ci) {
        if (!ArmorTrimEffects.DRAGONS_BREATH.appliesTo(betterTrims$getTrimmables())) return;
        NumberWrapper radius = NumberWrapper.zero();
        ArmorTrimEffects.DRAGONS_BREATH.apply(betterTrims$getTrimmables(), () -> radius.increment(ConfigManager.getConfig().dragonBreathRadius));

        AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(getWorld(), getX(), getY(), getZ());
        areaEffectCloud.setOwner((LivingEntity) (Object) this);
        areaEffectCloud.setDuration(1);
        areaEffectCloud.setRadius(radius.getFloat());
        activeStatusEffects.values().forEach(areaEffectCloud::addEffect);
        getWorld().spawnEntity(areaEffectCloud);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void addAbsorptionHearts(CallbackInfo ci) {
        stopwatch++;
        if (!ArmorTrimEffects.ENCHANTED_GOLDEN_APPLE.appliesTo(betterTrims$getTrimmables())) return;

        NumberWrapper ticksUntilHeal = NumberWrapper.of(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionDelay);
        NumberWrapper absorptionAmount = NumberWrapper.zero();
        ArmorTrimEffects.ENCHANTED_GOLDEN_APPLE.apply(betterTrims$getTrimmables(), () -> {
            ticksUntilHeal.decrement(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionDelayReduction);
            absorptionAmount.increment(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionAmount);
        });
        if (absorptionAmount.getFloat() > ConfigManager.getConfig().enchantedGoldenAppleEffects.maxAbsorption) {
            absorptionAmount.set(ConfigManager.getConfig().enchantedGoldenAppleEffects.maxAbsorption);
        }
        ticksUntilHeal.decrement(stopwatch);
        if (ticksUntilHeal.getInt() > 0) return;

        stopwatch = 0;
        if (getAbsorptionAmount() < absorptionAmount.getFloat()) {
            setAbsorptionAmount(absorptionAmount.getFloat());
        }
    }

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 0))
    private boolean shouldDodge(boolean original, DamageSource source, float amount) {
        if (original) return true;
        if (!ArmorTrimEffects.ENDER_PEARL.appliesTo(betterTrims$getTrimmables())) return false;

        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.ENDER_PEARL.apply(betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().enderPearlEffects.dodgeChance));
        if (chance.getFloat() > RandomHelper.nextFloat() || source.isIn(DamageTypeTags.IS_DROWNING)) {
            betterTrims$randomTpEntity((LivingEntity) (Object) this);
        }
        return false;
    }

    @ModifyReturnValue(method = "hurtByWater", at = @At("RETURN"))
    private boolean checkTrims(boolean original) {
        if (original) return true;
        return ConfigManager.getConfig().enderPearlEffects.waterDamagesUser && ArmorTrimEffects.ENDER_PEARL.appliesTo(betterTrims$getTrimmables());
    }
}
