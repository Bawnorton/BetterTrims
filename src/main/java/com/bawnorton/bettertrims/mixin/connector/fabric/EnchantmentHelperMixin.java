package com.bawnorton.bettertrims.mixin.connector.fabric;

import com.bawnorton.bettertrims.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.config.Config;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(EnchantmentHelper.class)
@ConditionalMixin(modid = "connectormod", applyIfPresent = false)
public abstract class EnchantmentHelperMixin {
    @ModifyExpressionValue(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getEnchantability()I"))
    private static int getTrimEnchantability(int original, Random random, ItemStack stack, int level, boolean treasureAllowed) {
        if (ArmorTrimEffects.LAPIS.appliesTo(stack)) return original + Config.getInstance().lapisEnchantability;
        return original;
    }
}
