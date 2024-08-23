package com.bawnorton.bettertrims.effect.attribute;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public record TrimAttribute(RegistryEntry<EntityAttribute> entry, double value, EntityAttributeModifier.Operation operation) {
    public static TrimAttribute adding(RegistryEntry<EntityAttribute> entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static TrimAttribute leveled(RegistryEntry<EntityAttribute> entry) {
        return new TrimAttribute(entry, 1, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static TrimAttribute multiplyBase(RegistryEntry<EntityAttribute> entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    public Identifier getSlotId(AttributeModifierSlot slot) {
        return Identifier.of("%s_trimmed_%s".formatted(entry.getIdAsString(), slot.asString()));
    }
}
