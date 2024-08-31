package com.bawnorton.bettertrims.mixin.attributes.trade_discount;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {
    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "prepareOffersFor",
            at = @At("TAIL")
    )
    private void applyTradeDiscount(PlayerEntity player, CallbackInfo ci) {
        for (TradeOffer offer : getOffers()) {
            int tradeCount = offer.getOriginalFirstBuyItem().getCount();
            double percentDiscount = player.getAttributeValue(TrimEntityAttributes.TRADE_DISCOUNT) - 1;
            double result = percentDiscount * tradeCount;
            int discount = (int) Math.ceil(result);
            offer.increaseSpecialPrice(-discount);
        }
    }
}