package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.BetterTrimsClient;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyConstant(method = "update", constant = @Constant(floatValue = 0.0F, ordinal = 1), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F")))
    private float improveNightVisionWhenWearingSilver(float original) {
        NumberWrapper increase = NumberWrapper.zero();
        if (client.player instanceof EntityExtender extender && extender.betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(extender.betterTrims$getTrimmables(), () -> increase.increment(BetterTrimsClient.getConfig().silverEffects.improveVision));
        }
        return original + increase.getFloat();
    }
}
