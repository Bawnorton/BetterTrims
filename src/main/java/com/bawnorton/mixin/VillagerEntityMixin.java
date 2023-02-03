package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Inject(method = "prepareOffersFor", at = @At("TAIL"))
    private void prepareOffersFor(PlayerEntity player, CallbackInfo ci) {
        double discount = 0;
        for(ItemStack stack: player.getArmorItems()) {
            if(ArmorTrimEffects.EMERALD.apply(stack)) {
                discount += 0.05;
            }
        }
        if(discount > 0) {
            for(TradeOffer offer: ((VillagerEntity)(Object)this).getOffers()) {
                offer.increaseSpecialPrice(-MathHelper.ceil(discount * offer.getOriginalFirstBuyItem().getCount()));
            }
        }
    }
}
