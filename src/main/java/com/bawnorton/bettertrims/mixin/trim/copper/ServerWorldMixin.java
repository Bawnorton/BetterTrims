package com.bawnorton.bettertrims.mixin.trim.copper;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
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
    private void applyCopperTrim(EntityList instance, Consumer<Entity> entityConsumer, Operation<Void> original) {
        instance.forEach(entity -> {
            if(!chunkManager.chunkLoadingManager.getTicketManager().shouldTickEntities(entity.getChunkPos().toLong())) return;
            if(!(entity instanceof LivingEntity livingEntity)) return;

            if(!TrimEffects.COPPER.matches(livingEntity)) {
                TrimEffects.COPPER.clearElectrified(livingEntity);
                return;
            }

            TrimEffects.COPPER.getApplicator().apply(TrimContext.empty(), livingEntity);
        });
        original.call(instance, entityConsumer);
    }
}
