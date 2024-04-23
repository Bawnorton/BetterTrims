package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.util.mixin.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArmorTrim.class)
@ConditionalMixin(modid = "better-trim-tooltips", applyIfPresent = false)
public abstract class ArmorTrimMixin {
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = At.Shift.AFTER))
    private static void addEffectTooltip(ItemStack stack, DynamicRegistryManager registryManager, List<Text> tooltip, CallbackInfo ci, @Local ArmorTrim trim) {
        if (tooltip.isEmpty()) return; // Shouldn't happen, but just in case

        ArmorTrimEffects.forEachAppliedEffect(stack, effect -> tooltip.add(
                        ScreenTexts.space()
                                   .append(
                                           effect.getTooltip()
                                                 .copy()
                                                 .fillStyle(
                                                         Style.EMPTY.withColor(
                                                                 tooltip.get(tooltip.size() - 1)
                                                                        .getStyle()
                                                                        .getColor()
                                                         )
                                                 )
                                   )
        ));
    }
}
