package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
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
    private void applyEmeraldTrimDiscount(PlayerEntity player, CallbackInfo ci) {
        NumberWrapper discount = NumberWrapper.zero();
        ArmorTrimEffects.EMERALD.apply(((LivingEntityExtender) player).betterTrims$getTrimmables(), () -> discount.increment(ConfigManager.getConfig().emeraldVillagerDiscount));
        if (discount.getFloat() > 0) {
            for (TradeOffer offer : ((VillagerEntity) (Object) this).getOffers()) {
                offer.increaseSpecialPrice(-MathHelper.ceil(discount.getFloat() * offer.getOriginalFirstBuyItem()
                                                                                       .getCount()));
            }
        }
    }
}
