package com.bawnorton.bettertrims.mixin.attributes.trade_discount;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancement.criterion.VillagerTradeCriterion;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin {
    @WrapOperation(
            method = "trade",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancement/criterion/VillagerTradeCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/passive/MerchantEntity;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void triggerBetterDeal(VillagerTradeCriterion instance, ServerPlayerEntity player, MerchantEntity merchant, ItemStack stack, Operation<Void> original) {
        original.call(instance, player, merchant, stack);
        if(player.getAttributeValue(TrimEntityAttributes.TRADE_DISCOUNT) - 1 > 0.25) {
            TrimCriteria.DISCOUNTED_TRADE.trigger(player);
        }
    }
}
