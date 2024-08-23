package com.bawnorton.bettertrims.mixin.trim.diamond;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
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
    private int applyDiamondTrim(int original, ItemStack stack, LootContext context) {
        if (!context.hasParameter(LootContextParameters.THIS_ENTITY)) return original;

        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if(!(entity instanceof LivingEntity livingEntity)) return original;
        if(!TrimEffects.DIAMOND.matches(entity)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.ENCHANTMENT_LEVEL, original);
        return TrimEffects.DIAMOND.getApplicator().apply(new TrimContext(builder), livingEntity).intValue();
    }
}
