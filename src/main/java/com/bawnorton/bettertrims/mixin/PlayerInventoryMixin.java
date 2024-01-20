package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public PlayerEntity player;

    @WrapOperation(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMiningSpeedMultiplier(Lnet/minecraft/block/BlockState;)F"))
    private float improveMiningSpeed(ItemStack instance, BlockState state, Operation<Float> original) {
        if (!(instance.getItem() instanceof MiningToolItem miningTool)) return original.call(instance, state);
        if (!miningTool.isSuitableFor(state)) return original.call(instance, state);

        NumberWrapper trimCount = NumberWrapper.zero();
        ArmorTrimEffects.IRON.apply(((LivingEntityExtender) player).betterTrims$getTrimmables(), () -> trimCount.increment(1));
        float percentageFaster = 1 + ConfigManager.getConfig().ironMiningSpeedIncrease * trimCount.getInt();
        return original.call(instance, state) * percentageFaster;
    }
}
