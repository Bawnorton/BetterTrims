package com.bawnorton.mixin;

import com.bawnorton.config.Config;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float modifySwimSpeed(float speed) {
        Wrapper<Float> increase = Wrapper.of(0f);
        ArmorTrimEffects.COPPER.apply(getArmorItems(), stack -> increase.set(increase.get() + Config.getInstance().copperSwimSpeedIncrease));
        return speed + increase.get();
    }

    @Inject(method = "getMovementSpeed()F", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        Wrapper<Float> increase = Wrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(getArmorItems(), stack -> increase.set(increase.get() + Config.getInstance().redstoneMovementSpeedIncrease));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }

    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float modifyDamage(float damage, float armor, float armorToughness, Operation<Float> original) {
        float orignal = original.call(damage, armor, armorToughness);
        Wrapper<Float> decrease = Wrapper.of(1f);
        ArmorTrimEffects.DIAMOND.apply(getArmorItems(), stack -> decrease.set(decrease.get() - Config.getInstance().diamondDamageReduction));
        return decrease.get() * orignal;
    }
}
