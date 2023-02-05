package com.bawnorton.mixin;

import com.bawnorton.effect.ArmorTrimEffects;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        cir.setReturnValue(ArmorTrimEffects.NETHERITE.appliesTo(getStack()));
    }
}
