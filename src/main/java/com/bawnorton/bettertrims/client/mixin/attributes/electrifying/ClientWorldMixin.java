package com.bawnorton.bettertrims.client.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.effect.CopperTrimEffect;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.util.FloodFill;
import com.bawnorton.bettertrims.util.Plane;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EntityList;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @ModifyReceiver(
            method = "tickEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/EntityList;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private EntityList applyElectrifying(EntityList instance, Consumer<Entity> action) {
        instance.forEach(entity -> {
            if(!(entity instanceof LivingEntity livingEntity)) return;

            CopperTrimEffect.ElectrifyingInfo info = TrimEffects.COPPER.electrifyingInfo(livingEntity);
            if (info == null) return;

            Random random = info.world().getRandom();
            Set<Pair<Vec3d, Direction>> walls = new HashSet<>();
            Set<Vec3d> edges = new HashSet<>();
            FloodFill.hollow(info.pos(), info.maxDist(), new HashSet<>(), walls, edges, info.isWall());
            edges.forEach(edge -> {
                float offsetX = random.nextFloat() - 0.5f;
                float offsetY = random.nextFloat() - 0.5f;
                float offsetZ = random.nextFloat() - 0.5f;
                BlockPos edgeBlockPos = BlockPos.ofFloored(edge);
                float fluidHeight = edgeBlockPos.getY() + info.world().getFluidState(edgeBlockPos).getHeight();
                bettertrims$spawnSpark(random, info.world(), edge.x + offsetX, fluidHeight + offsetY, edge.z + offsetZ);
            });
            walls.forEach(pair -> {
                Vec3d wall = pair.first();
                Direction direction = pair.right();
                BlockPos wallPos = BlockPos.ofFloored(wall);
                BlockState state = info.world().getBlockState(wallPos);
                VoxelShape collisionShape = state.getOutlineShape(info.world(), wallPos)
                        .offset(wallPos.getX(), wallPos.getY(), wallPos.getZ());
                List<Box> boundingBoxes = collisionShape.getBoundingBoxes();
                if (boundingBoxes.isEmpty()) {
                    BlockPos underWall = wallPos.down();
                    Plane plane = Plane.fromBlockPos(underWall, direction.getOpposite());
                    Vector3d randPoint = plane.getRandPointOnPlane();
                    float offsetX = random.nextFloat() - 0.5f;
                    float offsetZ = random.nextFloat() - 0.5f;
                    float fluidHeight = info.world().getFluidState(underWall).getHeight();
                    bettertrims$spawnSpark(random, info.world(), randPoint.x + offsetX, randPoint.y + fluidHeight, randPoint.z + offsetZ);
                } else {
                    boundingBoxes.forEach(box -> {
                        Plane plane = Plane.fromSideOfBox(box, direction.getOpposite());
                        Vector3d randPoint = plane.getRandPointOnPlane();
                        bettertrims$spawnSpark(random, info.world(), randPoint.x, randPoint.y, randPoint.z);
                    });
                }
            });
        });
        return instance;
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
