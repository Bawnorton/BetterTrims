package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.event.PreRegistryFreezeCallback;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin {
    @Shadow private boolean frozen;

    @Inject(
            method = "freeze",
            at = @At("HEAD")
    )
    private <T> void invokePreFreeze(CallbackInfoReturnable<Registry<T>> cir) {
        if(!frozen) {
            PreRegistryFreezeCallback.PRE.invoker().beforeFreeze((Registry<?>) this);
            PreRegistryFreezeCallback.POST.invoker().beforeFreeze((Registry<?>) this);
        }
    }
}
