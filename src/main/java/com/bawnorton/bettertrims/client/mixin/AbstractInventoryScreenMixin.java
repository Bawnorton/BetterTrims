package com.bawnorton.bettertrims.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin {
    @ModifyExpressionValue(
            method = "getStatusEffectDescription",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=9"
            )
    )
    private int allowHigherAmplifiers(int original) {
        return 15;
    }
}
