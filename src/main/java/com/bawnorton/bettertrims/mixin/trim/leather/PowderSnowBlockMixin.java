package com.bawnorton.bettertrims.mixin.trim.leather;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
    @WrapOperation(
            method = "canWalkOnPowderSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    private static boolean orIsTrimmed(ItemStack instance, Item item, Operation<Boolean> original) {
        if(original.call(instance, item)) return true;

        ArmorTrim trim = instance.get(DataComponentTypes.TRIM);
        if(trim == null) return false;

        return TrimEffects.LEATHER.matchesMaterial(trim.getMaterial());
    }
}
