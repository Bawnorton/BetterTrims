package com.bawnorton.bettertrims.mixin.trim.emerald;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
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

    @Inject(method = "prepareOffersFor", at = @At("TAIL"))
    private void applyEmeraldTrim(PlayerEntity player, CallbackInfo ci) {
        if(!TrimEffects.EMERALD.matches(player)) return;

        for (TradeOffer offer : getOffers()) {
            TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                    .add(TrimContextParameters.COUNT, offer.getOriginalFirstBuyItem().getCount());
            int discount = TrimEffects.EMERALD.getApplicator().apply(new TrimContext(builder), player);
            offer.increaseSpecialPrice(-discount);
        }
    }
}