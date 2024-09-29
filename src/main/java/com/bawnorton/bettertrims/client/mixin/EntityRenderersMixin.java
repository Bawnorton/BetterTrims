package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.render.AncientSkeletonEntityRenderer;
import com.bawnorton.bettertrims.registry.content.TrimEntities;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderers.class)
public abstract class EntityRenderersMixin {
    @Shadow
    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
        throw new AssertionError();
    }

    static {
        register(TrimEntities.ANCIENT_SKELETON, AncientSkeletonEntityRenderer::new);
    }
}
