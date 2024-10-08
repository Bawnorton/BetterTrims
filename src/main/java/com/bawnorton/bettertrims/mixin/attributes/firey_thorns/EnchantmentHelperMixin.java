package com.bawnorton.bettertrims.mixin.attributes.firey_thorns;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21
import net.minecraft.enchantment.EnchantmentLevelBasedValue;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    //? if >=1.21 {
    @Inject(
            method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD")
    )
    private static void applyFireyThorns(ServerWorld world, Entity target, DamageSource damageSource, ItemStack weapon, CallbackInfo ci) {
        if (!(target instanceof LivingEntity livingTarget)) return;

        int fireyThornsLevel = (int) livingTarget.getAttributeValue(TrimEntityAttributes.FIREY_THORNS);
        if (fireyThornsLevel <= 0) return;
        if (!(damageSource.getAttacker() instanceof LivingEntity attacker)) return;

        attacker.setOnFireFor(EnchantmentLevelBasedValue.linear(AttributeSettings.FireyThorns.base, AttributeSettings.FireyThorns.seconds).getValue(fireyThornsLevel));
    }
    //?} else {
    /*@Inject(
            method = "onTargetDamaged",
            at = @At("HEAD")
    )
    private static void applyFireyThorns(LivingEntity user, Entity target, CallbackInfo ci) {
        if (!(target instanceof LivingEntity livingTarget)) return;

        int fireyThornsLevel = (int) livingTarget.getAttributeValue(TrimEntityAttributes.FIREY_THORNS);
        if (fireyThornsLevel <= 0) return;

        user.setOnFireFor(AttributeSettings.FireyThorns.base + AttributeSettings.FireyThorns.seconds * fireyThornsLevel);
    }
    *///?}
}
