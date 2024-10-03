package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributeApplicator;
import com.bawnorton.bettertrims.extend.ItemStackExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExtender {
    @Unique
    private final ThreadLocal<LivingEntity> bettertrims$wearer = new ThreadLocal<>();

    @Override
    public void bettertrims$setWearer(LivingEntity wearer) {
        this.bettertrims$wearer.set(wearer);
    }

    @Override
    public LivingEntity bettertrims$getWearer() {
        return this.bettertrims$wearer.get();
    }

    //? if >=1.21 {
    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V",
            at = @At("HEAD")
    )
    private void captureWearer(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        bettertrims$setWearer(entity);
    }

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V",
            at = @At("TAIL")
    )
    private void releaseWearer(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        bettertrims$setWearer(null);
    }

    @ModifyReturnValue(
            method = "fromNbt",
            at = @At("RETURN")
    )
    private static Optional<ItemStack> addAttributesToTrims(Optional<ItemStack> original) {
        original.ifPresent(TrimEntityAttributeApplicator::apply);
        return original;
    }

    @Inject(
            method = {
                    "applyChanges",
                    "applyUnvalidatedChanges",
                    "applyComponentsFrom"
            },
            at = @At("TAIL")
    )
    private void addAttributesToTrims(CallbackInfo ci) {
        TrimEntityAttributeApplicator.apply((ItemStack) (Object) this);
    }
    //?} else {
    /*@WrapMethod(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V")
    private <T> void captureWearer(int amount, LivingEntity entity, Consumer<T> breakCallback, Operation<Void> original) {
        bettertrims$setWearer(entity);
        original.call(amount, entity, breakCallback);
        bettertrims$setWearer(null);
    }

    @ModifyReturnValue(
            method = "fromNbt",
            at = @At("RETURN")
    )
    private static ItemStack addAttributesToTrims(ItemStack original) {
        if(!original.isEmpty()) {
            TrimEntityAttributeApplicator.apply(original);
        }
        return original;
    }
    *///?}
}
