package com.bawnorton.bettertrims.mixin.attributes.thorns;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    //? if >=1.21 {
    /*@Inject(
            method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD")
    )
    private static void applyThorns(ServerWorld world, Entity target, DamageSource damageSource, ItemStack weapon, CallbackInfo ci) {
        if(damageSource.isOf(DamageTypes.THORNS)) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        int thornsLevel = (int) livingTarget.getAttributeValue(TrimEntityAttributes.THORNS);
        if (thornsLevel <= 0) return;
        if (!(damageSource.getAttacker() instanceof LivingEntity attacker)) return;

        attacker.damage(attacker.getWorld().getDamageSources().thorns(livingTarget), thornsLevel);
    }
    *///?} else {
    @Unique
    private static final ThreadLocal<LivingEntity> bettertrims$entityCapture = ThreadLocal.withInitial(() -> null);

    @Inject(
            method = "chooseEquipmentWith(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;Ljava/util/function/Predicate;)Ljava/util/Map$Entry;",
            at = @At("HEAD")
    )
    private static void captureEntity(Enchantment enchantment, LivingEntity entity, Predicate<ItemStack> condition, CallbackInfoReturnable<Map.Entry<EquipmentSlot, ItemStack>> cir) {
        bettertrims$entityCapture.set(entity);
    }

    @ModifyReturnValue(
            method = "getLevel",
            at = @At("RETURN")
    )
    private static int addThornsAttributes(int original) {
        LivingEntity livingEntity = bettertrims$entityCapture.get();
        if(livingEntity == null) return original;

        int thornsLevel = (int) livingEntity.getAttributeValue(TrimEntityAttributes.THORNS);
        return original + thornsLevel;
    }
    //?}
}
