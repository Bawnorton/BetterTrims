package com.bawnorton.bettertrims.mixin.trim.leather;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21
/*import net.minecraft.component.DataComponentTypes;*/

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
    //? if fabric {
    @WrapOperation(
            method = "canWalkOnPowderSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    private static boolean orIsTrimmed(ItemStack stack, Item item, Operation<Boolean> original, Entity entity) {
        if(original.call(stack, item)) return true;

        World world = entity.getWorld();
        ArmorTrim trim = /*$ trim_getter >>*/ ArmorTrim.getTrim(world.getRegistryManager(),stack).orElse(null);
        if(trim == null) return false;

        return TrimEffects.LEATHER.matchesMaterial(trim.getMaterial());
    }
    //?} elif neoforge {
    /*@WrapOperation(
            method = "canWalkOnPowderSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;canWalkOnPowderedSnow(Lnet/minecraft/entity/LivingEntity;)Z"
            )
    )
    private static boolean orIsTrimmed(ItemStack instance, LivingEntity livingEntity, Operation<Boolean> original) {
        if(original.call(instance, livingEntity)) return true;

        ArmorTrim trim = instance.get(DataComponentTypes.TRIM);
        if(trim == null) return false;

        return TrimEffects.LEATHER.matchesMaterial(trim.getMaterial());
    }
    *///?}
}
