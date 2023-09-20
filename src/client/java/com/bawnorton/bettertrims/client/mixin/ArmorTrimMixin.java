package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArmorTrim.class)
public abstract class ArmorTrimMixin {
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = At.Shift.AFTER))
    private static void addEffectTooltip(ItemStack stack, DynamicRegistryManager registryManager, List<Text> tooltip, CallbackInfo ci, @Local ArmorTrim trim) {
        ArmorTrimEffects.forEachAppliedEffect(stack, effect -> tooltip.add(ScreenTexts.space()
                                                                                   .append(effect.getTooltip()
                                                                                                   .copy()
                                                                                                   .fillStyle(trim.getMaterial()
                                                                                                                      .value()
                                                                                                                      .description()
                                                                                                                      .getStyle()))));
    }
}
