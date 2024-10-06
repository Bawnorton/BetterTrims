package com.bawnorton.bettertrims.mixin.attributes;

//? if <1.21 {
/*import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributeApplicator;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingTransformRecipeMixin {
    @WrapOperation(
            method = "craft",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setNbt(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    private void keepExistingAttributes(ItemStack instance, NbtCompound nbt, Operation<Void> original) {
        if(!(instance.getItem() instanceof Equipment equipment)) {
            original.call(instance, nbt);
            return;
        }

        Multimap<EntityAttribute, EntityAttributeModifier> existingModifiers = instance.getAttributeModifiers(equipment.getSlotType());
        original.call(instance, nbt);
        TrimEntityAttributeApplicator.apply(instance, existingModifiers);
    }
}
*///?}
