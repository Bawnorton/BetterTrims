package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @ModifyReturnValue(
            method = "createPlayerAttributes",
            at = @At("RETURN")
    )
    private static DefaultAttributeContainer.Builder addTrimAttributes(DefaultAttributeContainer.Builder original) {
        original.add(TrimEntityAttributes.BONUS_XP);
        original.add(TrimEntityAttributes.ENCHANTERS_FAVOUR);
        original.add(TrimEntityAttributes.FORTUNE);
        original.add(TrimEntityAttributes.MINERS_RUSH);
        original.add(TrimEntityAttributes.TRADE_DISCOUNT);
        return original;
    }
}
