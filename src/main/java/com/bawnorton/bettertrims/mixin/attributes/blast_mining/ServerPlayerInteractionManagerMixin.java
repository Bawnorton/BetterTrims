package com.bawnorton.bettertrims.mixin.attributes.blast_mining;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    //? if >=1.21 {
    /*@WrapOperation(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"
            )
    )
    private BlockState applyBlastMining(Block instance, World world, BlockPos pos, BlockState state, PlayerEntity player, Operation<BlockState> original) {
        BlockState centre = original.call(instance, world, pos, state, player);

        int depth = (int) player.getAttributeValue(TrimEntityAttributes.BLAST_MINING);
        if(depth <= 0) return centre;

        ItemStack held = player.getMainHandStack();
        if(!held.getItem().isCorrectForDrops(held, state)) return centre;

        Vec3d middle = bettertrims$mineBlocks(world, pos, state, player, depth, held);
        if(middle == null) return centre;

        world.createExplosion(
                player,
                Explosion.createDamageSource(world, player),
                null,
                middle.getX(),
                middle.getY(),
                middle.getZ(),
                depth * 3,
                false,
                World.ExplosionSourceType.NONE
        );

        return centre;
    }
    *///?} else {
    @WrapOperation(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void applyBlastMining(Block instance, World world, BlockPos pos, BlockState state, PlayerEntity player, Operation<BlockState> original) {
        original.call(instance, world, pos, state, player);

        int depth = (int) player.getAttributeValue(TrimEntityAttributes.BLAST_MINING);
        if(depth <= 0) return;

        ItemStack held = player.getMainHandStack();
        if(!held.getItem().isSuitableFor(held, state)) return;

        Vec3d middle = bettertrims$mineBlocks(world, pos, state, player, depth, held);
        if (middle == null) return;

        world.createExplosion(
                player,
                world.getDamageSources().explosion(player, player),
                null,
                middle.getX(),
                middle.getY(),
                middle.getZ(),
                depth * 3,
                false,
                World.ExplosionSourceType.NONE
        );
    }
    //?}

    @Unique
    private static @Nullable Vec3d bettertrims$mineBlocks(World world, BlockPos pos, BlockState state, PlayerEntity player, int depth, ItemStack held) {
        HitResult raycast = player.raycast(25, 1, false);
        if (!raycast.getType().equals(HitResult.Type.BLOCK)) return null;

        BlockHitResult blockRaycast = (BlockHitResult) raycast;
        Direction facing = blockRaycast.getSide().getOpposite();
        BlockPos end = pos.offset(facing, depth - 1);
        Iterable<BlockPos> toMine;
        if(facing.getAxis().isVertical()) {
            toMine = BlockPos.iterate(pos.west().north(), end.east().south());
        } else {
            toMine = BlockPos.iterate(
                    pos.down().offset(facing.rotateClockwise(Direction.Axis.Y)),
                    end.up().offset(facing.rotateCounterclockwise(Direction.Axis.Y))
            );
        }
        toMine.forEach(minePos -> {
            if(minePos.equals(pos)) return;
            if(player.isCreative()) {
                world.breakBlock(pos, false, null);
                return;
            }

            BlockState mined = world.getBlockState(minePos);
            //? if >=1.21 {
            /*if(!held.getItem().isCorrectForDrops(held, state)) return;
            *///?} else {
            if(!held.getItem().isSuitableFor(held, state)) return;
            //?}

            mined.getBlock().onBreak(world, pos, state, player);
            BlockEntity minedEntity = world.getBlockEntity(minePos);
            Block.dropStacks(mined, world, minePos, minedEntity, player, player.getMainHandStack());
            world.breakBlock(minePos, false, player);
        });

        return Vec3d.ofCenter(pos).add(Vec3d.ofCenter(end)).multiply(0.5f);
    }
}