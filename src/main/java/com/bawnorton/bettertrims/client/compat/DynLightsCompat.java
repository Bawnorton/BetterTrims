package com.bawnorton.bettertrims.client.compat;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class DynLightsCompat {
    public void init() {
        Registries.ENTITY_TYPE.stream()
                .filter(entityType -> !hasDynLightHandler(entityType))
                .forEach(entityType -> getRegistrar().accept(entityType, this::getLightLevel));
    }

    protected abstract boolean hasDynLightHandler(EntityType<? extends Entity> type);

    protected abstract BiConsumer<EntityType<? extends Entity>, Function<Entity, Integer>> getRegistrar();

    protected int getLightLevel(Entity entity) {
        if(entity instanceof LivingEntity livingEntity) {
            int glowing = (int) livingEntity.getAttributeValue(TrimEntityAttributes.GLOWING);
            return Math.min(15 / 4 * glowing, 15);
        }
        return 0;
    }
}
