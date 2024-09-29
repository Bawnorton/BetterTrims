package com.bawnorton.bettertrims.mixin.attributes.looting;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21
import net.minecraft.enchantment.EnchantmentLevelBasedValue;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    //? if >=1.21 {
    @ModifyReturnValue(
            method = "getEquipmentDropChance",
            at = @At("RETURN")
    )
    private static float applyLooting(float original, ServerWorld world, LivingEntity attacker, DamageSource damageSource, float baseEquipmentDropChance) {
        if (!(attacker instanceof PlayerEntity player)) return original;
        if (player.getAttributeValue(TrimEntityAttributes.LOOTING) <= 0) return original;

        return original + EnchantmentLevelBasedValue.linear(0.01f).getValue((int) player.getAttributeValue(TrimEntityAttributes.LOOTING));
    }
    //?} else {
    /*@ModifyReturnValue(
            method = "getLooting",
            at = @At("RETURN")
    )
    private static int applyLooting(int original, LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) return original;
        if (player.getAttributeValue(TrimEntityAttributes.LOOTING) <= 0) return original;

        return original + (int) player.getAttributeValue(TrimEntityAttributes.LOOTING);
    }
    *///?}
}
