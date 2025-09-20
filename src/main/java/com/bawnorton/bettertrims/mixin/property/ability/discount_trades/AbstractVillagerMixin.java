package com.bawnorton.bettertrims.mixin.property.ability.discount_trades;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment
@Mixin(AbstractVillager.class)
abstract class AbstractVillagerMixin extends AgeableMob {
    @Shadow
    public abstract MerchantOffers getOffers();

    AbstractVillagerMixin(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "setTradingPlayer",
        at = @At("TAIL")
    )
    private void applyTrimsToOffers(@Nullable Player player, CallbackInfo ci) {
        if(player == null) return;
        if(!(level() instanceof ServerLevel level)) return;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.TRADE_COST)) {
                for (MerchantOffer offer : getOffers()) {
                    int oldCost = offer.getBaseCostA().getCount();
                    int newCost = Math.round(ability.runEquipment(level, player, oldCost));
                    offer.addToSpecialPriceDiff(-(oldCost - newCost));
                }
            }
        }
    }
}

