package com.bawnorton.bettertrims.mixin.accessor;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Map;

@Mixin(DefaultAttributeContainer.class)
public interface DefaultAttributeContainerAccessor {
    //? if >=1.21 {
    @Accessor
    Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> getInstances();
    @Accessor
    @Mutable
    void setInstances(Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> instances);
    //?} else {
    /*@Accessor
    Map<EntityAttribute, EntityAttributeInstance> getInstances();
    @Accessor
    @Mutable
    void setInstances(Map<EntityAttribute, EntityAttributeInstance> instances);
    *///?}
}
