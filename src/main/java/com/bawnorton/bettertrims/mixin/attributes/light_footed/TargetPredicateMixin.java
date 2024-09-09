package com.bawnorton.bettertrims.mixin.attributes.light_footed;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TargetPredicate.class)
public abstract class TargetPredicateMixin {
    @ModifyReturnValue(
            method = "test",
            at = @At(value = "RETURN", ordinal = 0),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/LivingEntity;squaredDistanceTo(DDD)D"
                    )
            )
    )
    private boolean triggerSnuckByCreeper(boolean original, LivingEntity baseEntity, LivingEntity targetEntity) {
        if (!(baseEntity instanceof CreeperEntity) || !(targetEntity instanceof ServerPlayerEntity player)) return original;

        int lightFooted = (int) player.getAttributeValue(TrimEntityAttributes.LIGHT_FOOTED);
        if(lightFooted <= 0) return original;
        if(!player.isSneaking()) return original;

        double dist = baseEntity.squaredDistanceTo(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
        if(dist < 12) {
            TrimCriteria.SNUCK_BY_CREEPER.trigger(player);
        }
        return original;
    }
}
