package com.bawnorton.bettertrims.mixin.attributes.unbreaking;

import com.bawnorton.bettertrims.extend.ItemStackExtender;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExtender {
    //? if >=1.21 {
    @WrapMethod(method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
    private void captureWearer(int amount, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        bettertrims$setWearer(entity);
        original.call(amount, entity, slot);
        bettertrims$setWearer(null);
    }
    //?} else {
    /*@WrapMethod(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V")
    private <T> void captureWearer(int amount, LivingEntity entity, Consumer<T> breakCallback, Operation<Void> original) {
        bettertrims$setWearer(entity);
        original.call(amount, entity, breakCallback);
        bettertrims$setWearer(null);
    }
    *///?}
}
