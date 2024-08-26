package com.bawnorton.bettertrims.mixin.attributes.miners_rush;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.registry.content.TrimStatusEffects;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
    private void applyMinersRush(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if(world.isClient()) return;

        int minersRushLevel = (int) player.getAttributeValue(TrimEntityAttributes.MINERS_RUSH);
        if (state.isIn(ConventionalBlockTags.ORES)) {
            StatusEffectInstance existing = player.getStatusEffect(TrimStatusEffects.FEEL_THE_RUSH);
            int duration = (int) (20 * 2.5 * minersRushLevel);
            int maxAmplifier = (int) Math.pow(2, minersRushLevel) - 1;
            if (existing == null) {
                existing = new StatusEffectInstance(TrimStatusEffects.FEEL_THE_RUSH, duration, 0);
            } else {
                existing = new StatusEffectInstance(TrimStatusEffects.FEEL_THE_RUSH, duration, Math.min(maxAmplifier, existing.getAmplifier() + 1));
            }
            player.addStatusEffect(existing);
        }
    }
}