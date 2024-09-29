package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import com.bawnorton.bettertrims.registry.content.TrimEntities;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultAttributeRegistry.class)
public abstract class DefaultAttributeRegistryMixin {
    @ModifyReceiver(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
                    remap = false
            )
    )
    private static ImmutableMap.Builder<EntityType<? extends LivingEntity>, DefaultAttributeContainer> betterTrims$defaultAttributes(ImmutableMap.Builder<EntityType<? extends LivingEntity>, DefaultAttributeContainer> instance) {
        return instance.put(TrimEntities.ANCIENT_SKELETON, AncientSkeletonEntity.createAncientSkeletonAttributes().build());
    }
}
