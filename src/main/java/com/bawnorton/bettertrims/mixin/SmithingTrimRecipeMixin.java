package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.attribute.TrimEnityAttributeApplicator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
        if(type == DataComponentTypes.TRIM) {
            TrimEnityAttributeApplicator.apply(instance);
        }
        return result;
    }
}
