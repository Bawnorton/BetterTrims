package com.bawnorton.bettertrims.client.compat.sodiumdynlights;

import com.bawnorton.bettertrims.client.compat.DynLightsCompat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class SodiumDynLightsCompat extends DynLightsCompat {
    @Override
    protected BiConsumer<EntityType<? extends Entity>, Function<Entity, Integer>> getRegistrar() {
        try {
            return (type, lightGetter) -> DynamicLightHandlers.registerDynamicLightHandler(type, lightGetter::apply);
        } catch (NoClassDefFoundError e) { // now why would change your package name in minor patch
            return (type, lightGetter) -> toni.sodiumdynamiclights.api.DynamicLightHandlers.registerDynamicLightHandler(type, lightGetter::apply);
        }
    }

    @Override
    protected boolean hasDynLightHandler(EntityType<? extends Entity> type) {
        return DynamicLightHandlers.getDynamicLightHandler(type) != null;
    }
}
