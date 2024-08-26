package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.util.FloodFill;
import com.bawnorton.bettertrims.util.Plane;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EntityList;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow @Final private ServerChunkManager chunkManager;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/EntityList;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void applyElectrifying(EntityList instance, Consumer<Entity> entityConsumer, Operation<Void> original) {
        instance.forEach(entity -> {
            if(!chunkManager.chunkLoadingManager.getTicketManager().shouldTickEntities(entity.getChunkPos().toLong())) return;
            if(!(entity instanceof LivingEntity livingEntity)) return;

            if(livingEntity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING) <= 0) {
                TrimEffects.COPPER.clearElectrified(livingEntity);
                return;
            }

            World world = livingEntity.getWorld();

            Vec3d pos = livingEntity.getPos();
            BlockPos blockPos = BlockPos.ofFloored(pos);
            if (!world.isWater(blockPos))
                return;

            double electrifying = livingEntity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING);
            double maxDist = electrifying * 2;

            Predicate<Vec3d> isWall = vec3d -> {
                BlockPos wallPos = BlockPos.ofFloored(vec3d);
                BlockState state = world.getBlockState(wallPos);

                FluidState fluidState = state.getFluidState();
                boolean water = fluidState.isIn(FluidTags.WATER);
                boolean waterlogged = water && state.getOrEmpty(Properties.WATERLOGGED).orElse(false);
                if (waterlogged) {
                    return !wallPos.equals(BlockPos.ofFloored(pos));
                }

                return !water;
            };

            if (!world.isClient()) {
                Set<BlockPos> toElectrify = TrimEffects.COPPER.getElectrified().computeIfAbsent(livingEntity, k -> new HashSet<>());
                toElectrify.clear();
                Set<Vec3d> result = new HashSet<>();
                FloodFill.solid(pos, maxDist, result, isWall);
                result.forEach(vec3d -> toElectrify.add(BlockPos.ofFloored(vec3d)));
            } else {
                Random random = world.getRandom();
                Set<Pair<Vec3d, Direction>> walls = new HashSet<>();
                Set<Vec3d> edges = new HashSet<>();
                FloodFill.hollow(pos, maxDist, new HashSet<>(), walls, edges, isWall);
                edges.forEach(edge -> {
                    float offsetX = random.nextFloat() - 0.5f;
                    float offsetY = random.nextFloat() - 0.5f;
                    float offsetZ = random.nextFloat() - 0.5f;
                    BlockPos edgeBlockPos = BlockPos.ofFloored(edge);
                    float fluidHeight = edgeBlockPos.getY() + world.getFluidState(edgeBlockPos).getHeight();
                    bettertrims$spawnSpark(random, world, edge.x + offsetX, fluidHeight + offsetY, edge.z + offsetZ);
                });
                walls.forEach(pair -> {
                    Vec3d wall = pair.first();
                    Direction direction = pair.right();
                    BlockPos wallPos = BlockPos.ofFloored(wall);
                    BlockState state = world.getBlockState(wallPos);
                    VoxelShape collisionShape = state.getOutlineShape(world, wallPos)
                            .offset(wallPos.getX(), wallPos.getY(), wallPos.getZ());
                    List<Box> boundingBoxes = collisionShape.getBoundingBoxes();
                    if (boundingBoxes.isEmpty()) {
                        BlockPos underWall = wallPos.down();
                        Plane plane = Plane.fromBlockPos(underWall, direction.getOpposite());
                        Vector3d randPoint = plane.getRandPointOnPlane();
                        float offsetX = random.nextFloat() - 0.5f;
                        float offsetZ = random.nextFloat() - 0.5f;
                        float fluidHeight = world.getFluidState(underWall).getHeight();
                        bettertrims$spawnSpark(random, world, randPoint.x + offsetX, randPoint.y + fluidHeight, randPoint.z + offsetZ);
                    } else {
                        boundingBoxes.forEach(box -> {
                            Plane plane = Plane.fromSideOfBox(box, direction.getOpposite());
                            Vector3d randPoint = plane.getRandPointOnPlane();
                            bettertrims$spawnSpark(random, world, randPoint.x, randPoint.y, randPoint.z);
                        });
                    }
                });
            }
        });
        original.call(instance, entityConsumer);
    }

    @Unique
    private void bettertrims$spawnSpark(Random random, World world, double x, double y, double z) {
        int threshold = 10 + MinecraftClient.getInstance().options.getParticles().getValue().getId() * 10;
        if(random.nextInt(threshold) != 0) return;

        float dx = random.nextFloat() / 2 - 0.25f;
        float dy = random.nextFloat() / 2 - 0.25f;
        float dz = random.nextFloat() / 2 - 0.25f;
        world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                x, y, z,
                dx, dy, dz
        );
    }
}
