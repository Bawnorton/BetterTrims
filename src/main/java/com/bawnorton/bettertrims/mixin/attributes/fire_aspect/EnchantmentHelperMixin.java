package com.bawnorton.bettertrims.mixin.attributes.fire_aspect;

import com.bawnorton.bettertrims.effect.FireChargeTrimEffect;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(
            method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD")
    )
    private static void applyFireAspect(ServerWorld world, Entity target, DamageSource damageSource, ItemStack weapon, CallbackInfo ci) {
        if(damageSource.getAttacker() instanceof LivingEntity attacker) {
            int fireAspectLevel = (int) attacker.getAttributeValue(TrimEntityAttributes.FIRE_ASPECT);
            if(fireAspectLevel > 0) {
                target.setOnFireFor(EnchantmentLevelBasedValue.linear(AttributeSettings.FireAspect.base, AttributeSettings.FireAspect.seconds).getValue(fireAspectLevel));
            }
        }
    }
}
