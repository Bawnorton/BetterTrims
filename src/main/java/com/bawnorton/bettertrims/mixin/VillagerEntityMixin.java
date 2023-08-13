package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.Wrapper;
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
        NumberWrapper discount = NumberWrapper.of(0f);
        ArmorTrimEffects.EMERALD.apply(((EntityExtender) player).betterTrims$getTrimmables(), stack -> discount.increment(Config.getInstance().emeraldVillagerDiscount));
        if (discount.getFloat() > 0) {
            for (TradeOffer offer : ((VillagerEntity) (Object) this).getOffers()) {
                offer.increaseSpecialPrice(-MathHelper.ceil(discount.getFloat() * offer.getOriginalFirstBuyItem().getCount()));
            }
        }
    }
}