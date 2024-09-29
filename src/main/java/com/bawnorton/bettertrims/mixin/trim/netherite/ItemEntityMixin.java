package com.bawnorton.bettertrims.mixin.trim.netherite;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21
/*import net.minecraft.component.DataComponentTypes;*/

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @ModifyReturnValue(
            method = "isFireImmune",
            at = @At("RETURN")
    )
    private boolean netheriteTrimmedIsFireImmune(boolean original) {
        if(original) return true;

        ItemStack stack = getStack();
        World world = getWorld();
        ArmorTrim trim = /*$ trim_getter >>*/ ArmorTrim.getTrim(world.getRegistryManager(),stack).orElse(null);
        if(trim == null) return false;

        return TrimEffects.NETHERITE.matchesMaterial(trim.getMaterial());
    }
}
