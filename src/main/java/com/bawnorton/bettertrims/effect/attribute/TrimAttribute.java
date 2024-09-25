package com.bawnorton.bettertrims.effect.attribute;

import com.bawnorton.bettertrims.registry.AliasedRegistryEntry;
import com.google.common.base.Predicates;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

        //? if >=1.21 {
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.registry.entry.RegistryEntry;

public record TrimAttribute(Supplier<RegistryEntry<EntityAttribute>> entry, double value, EntityAttributeModifier.Operation operation, Predicate<AttributeModifierSlot> slotPredicate) {
    public static TrimAttribute adding(Supplier<RegistryEntry<EntityAttribute>> entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADD_VALUE, Predicates.alwaysTrue());
    }

    public static TrimAttribute leveled(Supplier<RegistryEntry<EntityAttribute>> entry) {
        return new TrimAttribute(entry, 1, EntityAttributeModifier.Operation.ADD_VALUE, Predicates.alwaysTrue());
    }

    public static TrimAttribute multiplyBase(Supplier<RegistryEntry<EntityAttribute>> entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE, Predicates.alwaysTrue());
    }

    public static TrimAttribute multiplyTotal(Supplier<RegistryEntry<EntityAttribute>> entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, Predicates.alwaysTrue());
    }

    public static TrimAttribute addingAliased(Supplier<AliasedRegistryEntry<EntityAttribute>> entry, double value) {
        return adding(() -> entry.get().getEntry(), value);
    }

    public static TrimAttribute leveledAliased(Supplier<AliasedRegistryEntry<EntityAttribute>> entry) {
        return leveled(() -> entry.get().getEntry());
    }

    public static TrimAttribute multiplyBaseAliased(Supplier<AliasedRegistryEntry<EntityAttribute>> entry, double value) {
        return multiplyBase(() -> entry.get().getEntry(), value);
    }

    public static TrimAttribute multiplyTotalAliased(Supplier<AliasedRegistryEntry<EntityAttribute>> entry, double value) {
        return multiplyTotal(() -> entry.get().getEntry(), value);
    }

    public Identifier getSlotId(AttributeModifierSlot slot) {
        if(entry.get() == null) throw new IllegalStateException("Trim attribute is not defined for %s".formatted(entry));

        return Identifier.of("%s_trimmed_%s".formatted(entry.get().getIdAsString(), slot.asString()));
    }

    public TrimAttribute forSlot(EquipmentSlot slot) {
        return new TrimAttribute(entry, value, operation, s -> s.equals(AttributeModifierSlot.forEquipmentSlot(slot)));
    }
}
//?} else {
/*import net.minecraft.entity.EquipmentSlot;

public record TrimAttribute(EntityAttribute entry, double value, EntityAttributeModifier.Operation operation, Predicate<EquipmentSlot> slotPredicate) {
    public static TrimAttribute adding(EntityAttribute entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.ADDITION, Predicates.alwaysTrue());
    }

    public static TrimAttribute leveled(EntityAttribute entry) {
        return new TrimAttribute(entry, 1, EntityAttributeModifier.Operation.ADDITION, Predicates.alwaysTrue());
    }

    public static TrimAttribute multiplyBase(EntityAttribute entry, double value) {
        return new TrimAttribute(entry, value, EntityAttributeModifier.Operation.MULTIPLY_BASE, Predicates.alwaysTrue());
    }

    public UUID getSlotId(EquipmentSlot slot) {
        String seed = "%s_trimmed_%s".formatted(Registries.ATTRIBUTE.getId(entry).toString(), slot.getName());
        Random rand = new Random(seed.hashCode());
        return new UUID(rand.nextLong(), rand.nextLong());
    }

    public TrimAttribute forSlot(EquipmentSlot slot) {
        return new TrimAttribute(entry, value, operation, s -> s.equals(slot));
    }
}
*///?}
