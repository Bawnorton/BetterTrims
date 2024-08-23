package com.bawnorton.bettertrims.mixin.trim.netherite;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @ModifyReturnValue(
            method = "isFireImmune",
            at = @At("RETURN")
    )
    private boolean netheriteTrimmedIsFireImmune(boolean original) {
        if(original) return true;

        ItemStack stack = getStack();
        ArmorTrim trim = stack.get(DataComponentTypes.TRIM);
        if(trim == null) return false;

        return TrimEffects.NETHERITE.matchesMaterial(trim.getMaterial());
    }
}
