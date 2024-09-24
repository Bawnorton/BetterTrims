package com.bawnorton.bettertrims.effect.attribute;

import com.bawnorton.bettertrims.registry.TrimRegistries;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;

//? if >=1.21 {
import com.bawnorton.bettertrims.mixin.accessor.AttributeModifiersComponentAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import com.bawnorton.bettertrims.data.tag.TrimEntityAttributeTags;
import java.util.ArrayList;
import java.util.List;
//?} else {
/*import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.DynamicRegistryManager;
*///?}


public class TrimEntityAttributeApplicator {
    //? if >=1.21 {
    public static void apply(ItemStack itemStack) {
        ArmorTrim trim = itemStack.get(DataComponentTypes.TRIM);
        if(trim == null) return;

        Item item = itemStack.getItem();
        if(!(item instanceof Equipment equipment)) return;

        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(equipment.getSlotType());
        var ref = new Object() {
            AttributeModifiersComponent attributeModifiers = itemStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        };

        ((AttributeModifiersComponentAccessor) (Object) ref.attributeModifiers).setModifiers(ref.attributeModifiers.modifiers()
                .stream()
                .filter(entry -> !entry.attribute().isIn(TrimEntityAttributeTags.ALL))
                .toList());

        for (AttributeModifiersComponent.Entry entry : item.getAttributeModifiers().modifiers()) {
            ref.attributeModifiers = ref.attributeModifiers.with(entry.attribute(), entry.modifier(), entry.slot());
        }

        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            if (trimEffect.matchesMaterial(trim.getMaterial())) {
                trimEffect.forEachAttribute(slot, (attribute, modifier) -> ref.attributeModifiers = ref.attributeModifiers.with(attribute, modifier, slot));
            } else {
                List<AttributeModifiersComponent.Entry> modifiers = new ArrayList<>(ref.attributeModifiers.modifiers());
                trimEffect.getEffectIds(slot).forEach(id -> modifiers.removeIf(entry -> entry.modifier().idMatches(id)));
                ((AttributeModifiersComponentAccessor) (Object) ref.attributeModifiers).setModifiers(List.copyOf(modifiers));
            }
        });
        itemStack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, ref.attributeModifiers);
    }
    //?} else {
    /*public static DynamicRegistryManager registryManager;

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
    *///?}
}
