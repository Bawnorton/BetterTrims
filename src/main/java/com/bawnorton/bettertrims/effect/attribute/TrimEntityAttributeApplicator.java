package com.bawnorton.bettertrims.effect.attribute;

import com.bawnorton.bettertrims.registry.TrimRegistries;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import java.util.ArrayList;
import java.util.List;

//? if >=1.21 {
/*import com.bawnorton.bettertrims.mixin.accessor.AttributeModifiersComponentAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
*///?}

public class TrimEntityAttributeApplicator {
    //? if >=1.21 {
    /*public static void apply(ItemStack itemStack) {
        ArmorTrim trim = itemStack.get(DataComponentTypes.TRIM);
        if(trim == null) return;

        Item item = itemStack.getItem();
        if(!(item instanceof Equipment equipment)) return;

        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(equipment.getSlotType());
        AttributeModifiersComponent[] component = {itemStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)};
        for (AttributeModifiersComponent.Entry entry : item.getAttributeModifiers().modifiers()) {
            component[0] = component[0].with(entry.attribute(), entry.modifier(), entry.slot());
        }
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            if (trimEffect.matchesMaterial(trim.getMaterial())) {
                trimEffect.forEachAttribute(slot, (attribute, modifier) -> component[0] = component[0].with(attribute, modifier, slot));
            } else {
                List<AttributeModifiersComponent.Entry> modifiers = new ArrayList<>(component[0].modifiers());
                trimEffect.getEffectIds(slot).forEach(id -> modifiers.removeIf(entry -> entry.modifier().idMatches(id)));
                ((AttributeModifiersComponentAccessor) (Object) component[0]).setModifiers(List.copyOf(modifiers));
            }
        });
        itemStack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, component[0]);
    }
    *///?} else {
    public static DynamicRegistryManager registryManager;

    public static void apply(ItemStack itemStack) {
        ArmorTrim trim = ArmorTrim.getTrim(registryManager, itemStack).orElse(null);
        if(trim == null) return;

        Item item = itemStack.getItem();
        if(!(item instanceof Equipment equipment)) return;

        EquipmentSlot slot = equipment.getSlotType();
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = itemStack.getAttributeModifiers(slot);
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            if (trimEffect.matchesMaterial(trim.getMaterial())) {
                trimEffect.forEachAttribute(slot, modifiers::put);
            } else {
                Multimap<EntityAttribute, EntityAttributeModifier> toRemove = HashMultimap.create();
                trimEffect.getEffectIds(slot).forEach(id -> modifiers.entries()
                        .stream()
                        .filter(entry -> entry.getValue().getId().equals(id))
                        .forEach(entry -> toRemove.put(entry.getKey(), entry.getValue()))
                );
                toRemove.forEach(modifiers::remove);
            }
        });
        itemStack.removeSubNbt("AttributeModifiers");
        modifiers.forEach((attribute, modifier) -> itemStack.addAttributeModifier(attribute, modifier, slot));
    }
    //?}
}
