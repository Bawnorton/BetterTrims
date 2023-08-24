package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow public abstract AttributeContainer getAttributes();

    @Shadow @Final private AttributeContainer attributes;

    @Shadow protected abstract float modifyAppliedDamage(DamageSource source, float amount);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow public abstract float getStepHeight();

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float applyTrimSwimSpeedIncrease(float speed) {
        NumberWrapper increase = NumberWrapper.zero();
        ArmorTrimEffects.COPPER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().copperSwimSpeedIncrease));
        return speed + increase.getFloat();
    }

    @ModifyReturnValue(method = "getMovementSpeed()F", at = @At("RETURN"))
    private float applyTrimSpeedIncrease(float original) {
        if(((LivingEntity) (Object) this) instanceof AbstractHorseEntity horseEntity) {
            if(horseEntity.getControllingPassenger() instanceof PlayerEntity player) {
                NumberWrapper increase = NumberWrapper.one();
                ArmorTrimEffects.REDSTONE.apply(((EntityExtender) player).betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().redstoneMovementSpeedIncrease));
                if(betterTrims$shouldSilverApply()) {
                    ArmorTrimEffects.SILVER.apply(((EntityExtender) player).betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.movementSpeed));
                }
                return original * increase.getFloat();
            }
        }
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().redstoneMovementSpeedIncrease));
        if(betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.movementSpeed));
        }
        return original * increase.getFloat();
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 2), slice =
    @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z")
    ))
    private Vec3d applyTrimSpeedIncrease(Vec3d original) {
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().redstoneMovementSpeedIncrease));
        if(betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.movementSpeed));
        }
        return original.multiply(increase.getFloat(), 1, increase.getFloat());
    }

    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float applyTrimDamageReduction(float damage, float armor, float armorToughness, Operation<Float> original) {
        float orignal = original.call(damage, armor, armorToughness);
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.DIAMOND.apply(betterTrims$getTrimmables(), stack -> decrease.decrement(Config.getInstance().diamondDamageReduction));
        if(betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> decrease.decrement(Config.getInstance().silverNightBonus.damageReduction));
        }
        return decrease.getFloat() * orignal;
    }

    @ModifyReturnValue(method = "getJumpVelocity", at = @At("RETURN"))
    private float applyTrimJumpHeight(float original) {
        if(betterTrims$shouldSilverApply()) {
            NumberWrapper increase = NumberWrapper.zero();
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().silverNightBonus.jumpHeight));
            return original + increase.getFloat();
        }
        return original;
    }

    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    protected void canTargetOverride(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        // overriden in IllagerEntityMixin
    }

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"))
    private Entity applyFireChargeTrim(Entity original) {
        NumberWrapper duration = NumberWrapper.zero();
        ArmorTrimEffects.FIRE_CHARGE.apply(betterTrims$getTrimmables(), stack -> duration.increment(Config.getInstance().fireChargeFireDuration));
        if(duration.getFloat() > 0) {
            original.setOnFireFor(duration.getInt());
        }
        return original;
    }

    @ModifyReturnValue(method = "computeFallDamage", at = @At("RETURN"))
    private int applyTrimFallDamageReduction(int original) {
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), stack -> decrease.decrement(Config.getInstance().slimeBallEffects.fallDamageReduction));
        return (int) (original * decrease.getFloat());
    }

    @ModifyExpressionValue(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double applyTrimKnockbackDecrease(double original) {
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), stack -> decrease.decrement(Config.getInstance().slimeBallEffects.knockbacIncrease));
        return original * decrease.getFloat();
    }
}
