package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.effect.CopperTrimEffect;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.util.FloodFill;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
            //? if >=1.21 {
            if(!chunkManager.chunkLoadingManager.getTicketManager().shouldTickEntities(entity.getChunkPos().toLong())) return;
            //?} else {
            /*if(!chunkManager.threadedAnvilChunkStorage.getTicketManager().shouldTickEntities(entity.getChunkPos().toLong())) return;
            *///?}
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
