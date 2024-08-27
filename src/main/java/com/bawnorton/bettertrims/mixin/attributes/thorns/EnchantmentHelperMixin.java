package com.bawnorton.bettertrims.mixin.attributes.thorns;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
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
    private static void applyThorns(ServerWorld world, Entity target, DamageSource damageSource, ItemStack weapon, CallbackInfo ci) {
        if(damageSource.isOf(DamageTypes.THORNS)) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        int thornsLevel = (int) livingTarget.getAttributeValue(TrimEntityAttributes.THORNS);
        if (thornsLevel <= 0) return;
        if (!(damageSource.getAttacker() instanceof LivingEntity attacker)) return;

        attacker.damage(attacker.getWorld().getDamageSources().thorns(livingTarget), thornsLevel);
    }
}
