package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        Wrapper<Float> discount = new Wrapper<>(0f);
        ArmorTrimEffects.EMERALD.apply(player.getArmorItems(), stack -> discount.set(discount.get() + BetterTrims.CONFIG.emeraldVillagerDiscount));
        if (discount.get() > 0) {
            for (TradeOffer offer : ((VillagerEntity) (Object) this).getOffers()) {
                offer.increaseSpecialPrice(-MathHelper.ceil(discount.get() * offer.getOriginalFirstBuyItem().getCount()));
            }
        }
    }
}
