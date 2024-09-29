package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributeApplicator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21 {
/*import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
*///?}

@Mixin(SmithingTrimRecipe.class)
public abstract class SmithingTrimRecipeMixin {
    //? if >=1.21 {
    /*@SuppressWarnings("MixinExtrasOperationParameters")
    @WrapOperation(
            method = "craft(Lnet/minecraft/recipe/input/SmithingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private <T> T addAttributeModifiers(ItemStack instance, ComponentType<? super T> type, T value, Operation<T> original) {
        T result = original.call(instance, type, value);
        if(type == DataComponentTypes.TRIM) {
            TrimEntityAttributeApplicator.apply(instance);
        }
        return result;
    }
    *///?} else {
    @WrapOperation(
            method = "craft",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/trim/ArmorTrim;apply(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/trim/ArmorTrim;)Z"
            )
    )
    private boolean addAttributeModifiers(DynamicRegistryManager instance, ItemStack stack, ArmorTrim trim, Operation<Boolean> original) {
        boolean result = original.call(instance, stack, trim);
        if(result) {
            TrimEntityAttributeApplicator.apply(stack);
        }
        return result;
    }
    //?}
}
