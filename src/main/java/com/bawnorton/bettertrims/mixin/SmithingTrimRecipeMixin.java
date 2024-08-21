package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.mixin.accessor.AttributeModifiersComponentAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.recipe.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.List;

@Mixin(SmithingTrimRecipe.class)
public abstract class SmithingTrimRecipeMixin {
    @SuppressWarnings("MixinExtrasOperationParameters")
    @WrapOperation(
            method = "craft(Lnet/minecraft/recipe/input/SmithingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private <T> T addAttributeModifiers(ItemStack instance, ComponentType<? super T> type, T value, Operation<T> original) {
        T result = original.call(instance, type, value);
        if(type != DataComponentTypes.TRIM) return result;

        ArmorTrim trim = (ArmorTrim) value;
        Item item = instance.getItem();
        if(!(item instanceof Equipment equipment)) return result;

        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(equipment.getSlotType());
        AttributeModifiersComponent[] component = {instance.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)};
        for (AttributeModifiersComponent.Entry entry : instance.getItem().getAttributeModifiers().modifiers()) {
            component[0] = component[0].with(entry.attribute(), entry.modifier(), entry.slot());
        }
        TrimEffects.forEachTrimEffect(trimEffect -> {
            if (trimEffect.matchesMaterial(trim.getMaterial())) {
                trimEffect.forEachAttribute(slot, (attribute, modifier) -> component[0] = component[0].with(attribute, modifier, slot));
            } else {
                List<AttributeModifiersComponent.Entry> modifiers = new ArrayList<>(component[0].modifiers());
                trimEffect.getEffectIds(slot).forEach(id -> modifiers.removeIf(entry -> entry.modifier().idMatches(id)));
                ((AttributeModifiersComponentAccessor) (Object) component[0]).setModifiers(List.copyOf(modifiers));
            }
        });
        instance.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, component[0]);
        return result;
    }
}
