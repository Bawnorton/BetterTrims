package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import com.bawnorton.bettertrims.registry.content.TrimEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
    static {
        TrimEntities.ANCIENT_SKELETON = bettertrims$register(
                "ancient_skeleton",
                EntityType.Builder.create(AncientSkeletonEntity::new, SpawnGroup.CREATURE)
                        //? if >=1.21 {
                        .dimensions(0.6F, 2F)
                        .eyeHeight(1.74F)
                        //?} else {
                        /*.setDimensions(0.6F, 2F)
                        *///?}
                        .maxTrackingRange(8)
        );
    }

    @Unique
    private static <T extends Entity> EntityType<T> bettertrims$register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, BetterTrims.id(id), type.build(id));
    }
}
