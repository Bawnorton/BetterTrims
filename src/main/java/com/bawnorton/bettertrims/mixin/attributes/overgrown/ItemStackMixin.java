package com.bawnorton.bettertrims.mixin.attributes.overgrown;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @WrapOperation(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V"
            )
    )
    private void applyOvergrown(Item instance, ItemStack stack, World world, Entity entity, int slot, boolean selected, Operation<Void> original) {
        original.call(instance, stack, world, entity, slot, selected);
        if(!(entity instanceof LivingEntity livingEntity)) return;
        if(world.isClient()) return;
        if(!stack.isDamageable()) return;
        //? if >=1.21 {
        /*if(!selected && !Iterables.contains(livingEntity.getEquippedItems(), stack)) return;
        *///?} else {
        if(!selected && !Iterables.contains(livingEntity.getItemsEquipped(), stack)) return;
        //?}

        double overgrown = livingEntity.getAttributeValue(TrimEntityAttributes.OVERGROWN);
        double chance = AttributeSettings.Overgrown.chanceToRepair * overgrown;
        if(BetterTrims.PROBABILITIES.passes(chance)) {
            stack.setDamage(stack.getDamage() - 1);
        }
    }
}
