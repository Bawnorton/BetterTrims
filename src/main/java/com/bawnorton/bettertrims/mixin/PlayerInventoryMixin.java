package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
    private float improveMiningSpeed(float original) {
        System.out.println("original: " + original);
        NumberWrapper trimCount = NumberWrapper.zero();
        ArmorTrimEffects.IRON.apply(((EntityExtender) player).betterTrims$getTrimmables(), () -> trimCount.increment(1));
        float percentageFaster = ConfigManager.getConfig().ironMiningSpeedIncrease * trimCount.getInt();
        System.out.println("percentageFaster: " + percentageFaster);
        System.out.println("returning: " + original * (1 + percentageFaster));
        return original * (1 + percentageFaster);
    }
}
