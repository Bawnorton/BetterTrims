package com.bawnorton.bettertrims.client.compat.lambdynlights;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;

public final class LambDynLightsCompat implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        Registries.ENTITY_TYPE.stream()
                .filter(livingEntityType -> DynamicLightHandlers.getDynamicLightHandler(livingEntityType) == null)
                .forEach(livingEntityType -> DynamicLightHandlers.registerDynamicLightHandler(livingEntityType, entity -> {
                    if(entity instanceof LivingEntity livingEntity) {
                        int glowing = (int) livingEntity.getAttributeValue(TrimEntityAttributes.GLOWING);
                        return Math.min(15 / 4 * glowing, 15);
                    }
                    return 0;
                }));
    }
}
