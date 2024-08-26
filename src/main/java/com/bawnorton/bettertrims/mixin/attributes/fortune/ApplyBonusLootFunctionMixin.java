package com.bawnorton.bettertrims.mixin.attributes.fortune;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ApplyBonusLootFunction.class)
public abstract class ApplyBonusLootFunctionMixin {
    @ModifyExpressionValue(
            method = "process",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/item/ItemStack;)I"
            )
    )
    private int applyFortune(int original, ItemStack stack, LootContext context) {
        if (!context.hasParameter(LootContextParameters.THIS_ENTITY)) return original;

        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if(!(entity instanceof LivingEntity livingEntity)) return original;

        return (int) (original + livingEntity.getAttributeValue(TrimEntityAttributes.FORTUNE));
    }
}
