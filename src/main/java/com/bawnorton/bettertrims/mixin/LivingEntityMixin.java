package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float modifySwimSpeed(float speed) {
        Wrapper<Float> increase = Wrapper.of(0f);
        ArmorTrimEffects.COPPER.apply(betterTrims$getTrimmables(), stack -> increase.set(increase.get() + Config.getInstance().copperSwimSpeedIncrease));
        return speed + increase.get();
    }

    @Inject(method = "getMovementSpeed()F", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if(((LivingEntity) (Object) this) instanceof AbstractHorseEntity horseEntity) {
            if(horseEntity.getControllingPassenger() instanceof PlayerEntity player) {
                Wrapper<Float> increase = Wrapper.of(1f);
                ArmorTrimEffects.REDSTONE.apply(((EntityExtender) player).betterTrims$getTrimmables(), stack -> increase.set(increase.get() + Config.getInstance().redstoneMovementSpeedIncrease));
                cir.setReturnValue(cir.getReturnValue() * increase.get());
            }
        }
        Wrapper<Float> increase = Wrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), stack -> increase.set(increase.get() + Config.getInstance().redstoneMovementSpeedIncrease));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 2), slice =
    @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 1)
    ))
    private Vec3d modifyFlightSpeed(Vec3d original) {
        Wrapper<Float> increase = Wrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), stack -> increase.set(increase.get() + Config.getInstance().redstoneMovementSpeedIncrease));
        return original.multiply(increase.get(), 1, increase.get());
    }

    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float modifyDamage(float damage, float armor, float armorToughness, Operation<Float> original) {
        float orignal = original.call(damage, armor, armorToughness);
        Wrapper<Float> decrease = Wrapper.of(1f);
        ArmorTrimEffects.DIAMOND.apply(betterTrims$getTrimmables(), stack -> decrease.set(decrease.get() - Config.getInstance().diamondDamageReduction));
        return decrease.get() * orignal;
    }

    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    protected void canTargetOverride(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {

    }
}
