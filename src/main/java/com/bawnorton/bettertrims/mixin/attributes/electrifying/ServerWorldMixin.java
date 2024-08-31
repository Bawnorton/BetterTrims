package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.effect.CopperTrimEffect;
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
            if (!(entity instanceof LivingEntity livingEntity)) return;

            CopperTrimEffect.ElectrifyingInfo info = TrimEffects.COPPER.electrifyingInfo(livingEntity);
            if(info == null) return;

            Set<BlockPos> toElectrify = TrimEffects.COPPER.getElectrified().computeIfAbsent(livingEntity, k -> new HashSet<>());
            toElectrify.clear();
            Set<Vec3d> result = new HashSet<>();
            FloodFill.solid(info.pos(), info.maxDist(), result, info.isWall());
            result.forEach(vec3d -> toElectrify.add(BlockPos.ofFloored(vec3d)));
        });
        original.call(instance, entityConsumer);
    }
}
