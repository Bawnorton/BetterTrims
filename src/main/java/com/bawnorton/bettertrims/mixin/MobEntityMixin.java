package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntityMixin {
    @WrapOperation(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    protected boolean shouldHit(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        return original.call(instance, source, amount); // overriden in EndermanEntityMixin
    }

    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"))
    private int addFireFromTrim(int original) {
        NumberWrapper duration = NumberWrapper.of(original);
        ArmorTrimEffects.FIRE_CHARGE.apply(betterTrims$getTrimmables(), stack -> duration.increment(1));
        return Math.max(original + duration.getInt(), Enchantments.FIRE_ASPECT.getMaxLevel() * 4);
    }
}
