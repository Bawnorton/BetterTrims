package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float modifySwimSpeed(float speed) {
        Wrapper<Float> increase = Wrapper.of(0f);
        ArmorTrimEffects.COPPER.apply(getArmorItems(), stack -> increase.set(increase.get() + BetterTrims.CONFIG.copperSwimSpeedIncrease));
        return speed + increase.get();
    }

    @Inject(method = "getMovementSpeed()F", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        Wrapper<Float> increase = Wrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(getArmorItems(), stack -> increase.set(increase.get() + BetterTrims.CONFIG.redstoneMovementSpeedIncrease));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }

    @Redirect(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float modifyDamage(float damage, float armor, float armorToughness) {
        float orignal = DamageUtil.getDamageLeft(damage, armor, armorToughness);
        Wrapper<Float> decrease = Wrapper.of(1f);
        ArmorTrimEffects.DIAMOND.apply(getArmorItems(), stack -> decrease.set(decrease.get() - BetterTrims.CONFIG.diamondDamageReduction));
        return decrease.get() * orignal;
    }
}
