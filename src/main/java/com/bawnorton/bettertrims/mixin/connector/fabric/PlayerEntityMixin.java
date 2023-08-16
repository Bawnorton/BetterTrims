package com.bawnorton.bettertrims.mixin.connector.fabric;

import com.bawnorton.bettertrims.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
@ConditionalMixin(modid = "connectormod", applyIfPresent = false)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @WrapOperation(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F"))
    private float modifyMiningSpeed(PlayerInventory instance, BlockState block, Operation<Float> original) {
        NumberWrapper increase = NumberWrapper.of(original.call(instance, block));
        ArmorTrimEffects.IRON.apply(betterTrims$getTrimmables(), stack -> {
            if(instance.getMainHandStack().isSuitableFor(block)) {
                increase.increment(Config.getInstance().ironMiningSpeedIncrease);
            }
        });
        return increase.getFloat();
    }
}
