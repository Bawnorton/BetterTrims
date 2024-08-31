package com.bawnorton.bettertrims.mixin.attributes.bouncy;

import com.bawnorton.bettertrims.effect.SlimeTrimEffect;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @WrapOperation(
            method = "fall",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"
            )
    )
    private void applyBouncy(Block instance, World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, Operation<Void> original) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            original.call(instance, world, state, pos, entity, fallDistance);
            return;
        }
        if(!SlimeTrimEffect.bouncyBoots) return;

        int bouncy = (int) livingEntity.getAttributeValue(TrimEntityAttributes.BOUNCY);
        if (bouncy < 1) {
            original.call(instance, world, state, pos, entity, fallDistance);
            return;
        }

        Blocks.SLIME_BLOCK.onLandedUpon(world, state, pos, entity, fallDistance);
    }

    @WrapOperation(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"
            )
    )
    private void applyBouncy(Block instance, BlockView world, Entity entity, Operation<Void> original) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            original.call(instance, world, entity);
            return;
        }
        if(!SlimeTrimEffect.bouncyBoots) return;

        int bouncy = (int) livingEntity.getAttributeValue(TrimEntityAttributes.BOUNCY);
        if(bouncy < 1) {
            original.call(instance, world, entity);
            return;
        }

        Blocks.SLIME_BLOCK.onEntityLand(world, entity);
    }
}
