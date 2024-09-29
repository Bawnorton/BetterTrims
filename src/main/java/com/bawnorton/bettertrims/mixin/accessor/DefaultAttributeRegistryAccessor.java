package com.bawnorton.bettertrims.mixin.accessor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Map;

@Mixin(DefaultAttributeRegistry.class)
public interface DefaultAttributeRegistryAccessor {
    @Accessor("DEFAULT_ATTRIBUTE_REGISTRY")
    static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer> getRegistry() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("DEFAULT_ATTRIBUTE_REGISTRY")
    static void setRegistry(Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer> registry) {
        throw new AssertionError();
    }
}
