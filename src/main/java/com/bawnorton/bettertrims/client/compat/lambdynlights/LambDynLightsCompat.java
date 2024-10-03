package com.bawnorton.bettertrims.client.compat.lambdynlights;

//? if fabric {

/*import com.bawnorton.bettertrims.client.compat.DynLightsCompat;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class LambDynLightsCompat extends DynLightsCompat {
    @Override
    protected BiConsumer<EntityType<? extends Entity>, Function<Entity, Integer>> getRegistrar() {
        return (type, lightGetter) -> DynamicLightHandlers.registerDynamicLightHandler(type, lightGetter::apply);
    }

    @Override
    protected boolean hasDynLightHandler(EntityType<? extends Entity> type) {
        return DynamicLightHandlers.getDynamicLightHandler(type) != null;
    }
}
*///?} else {
public final class LambDynLightsCompat {
    public void init() {
    }
}
//?}
