package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.attribute.TrimEnityAttributeApplicator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @ModifyReturnValue(
            method = "fromNbt",
            at = @At("RETURN")
    )
    private static Optional<ItemStack> addAttributesToTrims(Optional<ItemStack> original) {
        original.ifPresent(TrimEnityAttributeApplicator::apply);
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
        TrimEnityAttributeApplicator.apply((ItemStack) (Object) this);
    }
}
