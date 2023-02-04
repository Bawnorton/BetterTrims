package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float modifySwimSpeed(float speed) {
        Wrapper<Float> increase = Wrapper.of(0F);
        ArmorTrimEffects.COPPER.apply(getArmorItems(), stack -> increase.set(increase.get() + 0.05F));
        return speed + increase.get();
    }

    @Inject(method = "getMovementSpeed()F", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        Wrapper<Float> increase = Wrapper.of(1F);
        ArmorTrimEffects.REDSTONE.apply(getArmorItems(), stack -> increase.set(increase.get() + 0.15F));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }

    @Redirect(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float modifyDamage(float damage, float armor, float armorToughness) {
        Wrapper<Float> decrease = Wrapper.of(DamageUtil.getDamageLeft(damage, armor, armorToughness));
        ArmorTrimEffects.DIAMOND.apply(getArmorItems(), stack -> decrease.set(decrease.get() - 0.05F));
        return damage * decrease.get();
    }
}
