package com.bawnorton.bettertrims.client.mixin.attributes.regeneration;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyExpressionValue(
            //? if fabric {
            /*method = "renderStatusBars",
            *///?} elif neoforge {
            method = "renderHealthLevel",
            //?}
            at = @At(
                    value = "INVOKE",
                    //? if >=1.21 {
                    target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
                    //?} else {
                    /*target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z",
                    ordinal = 0
                    *///?}
            )
    )
    private boolean orHasRegenAttribute(boolean original) {
        return original || client.player.getAttributeValue(TrimEntityAttributes.REGENERATION) > 0;
    }
}
