package com.bawnorton.bettertrims.mixin.trim.diamond;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(
            method = "afterBreak",
            at = @At("HEAD")
    )
    private void applyDiamondTrim(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if(world.isClient()) return;
        if(!TrimEffects.DIAMOND.matches(player)) return;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.BLOCK_STATE, state);
        TrimEffects.DIAMOND.getApplicator().apply(new TrimContext(builder), player);
    }
}
