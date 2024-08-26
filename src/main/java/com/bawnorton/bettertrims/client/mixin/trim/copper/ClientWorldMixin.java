package com.bawnorton.bettertrims.client.mixin.trim.copper;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.function.Consumer;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @WrapOperation(
            method = "tickEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/EntityList;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void applyCopperTrim(EntityList instance, Consumer<Entity> entityConsumer, Operation<Void> original) {
        instance.forEach(entity -> {
            if (!(entity instanceof LivingEntity livingEntity)) return;

            if(livingEntity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING) <= 0) {
                TrimEffects.COPPER.clearElectrified(livingEntity);
                return;
            }

            TrimEffects.COPPER.getApplicator().apply(TrimContext.empty(), livingEntity);
        });
        original.call(instance, entityConsumer);
    }
}
