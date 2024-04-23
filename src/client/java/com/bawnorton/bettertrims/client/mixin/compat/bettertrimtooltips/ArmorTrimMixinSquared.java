package com.bawnorton.bettertrims.client.mixin.compat.bettertrimtooltips;

import com.bawnorton.bettertrims.util.mixin.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ArmorTrim.class, priority = 1500)
@ConditionalMixin(modid = "better-trim-tooltips")
public abstract class ArmorTrimMixinSquared {
    @TargetHandler(
            mixin = "io.github.andrew6rant.bettertrimtooltips.mixin.client.ArmorTrimMixin",
            name = "appendTrimTooltip"
    )
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z"))
    private static Object addEffectTooltip(Object tooltip, @Local(argsOnly = true) ArmorTrim trim) {
        if(ArmorTrimEffects.getEffects(trim).isEmpty()) return tooltip;

        MutableText effectTooltip = (MutableText) tooltip;
        if (!Screen.hasShiftDown()) {
            effectTooltip.append(Text.of(" §b+§r"));
        } else {
            effectTooltip.append(Text.of(" §c-§r"));
        }
        return effectTooltip;
    }

    @TargetHandler(
            mixin = "io.github.andrew6rant.bettertrimtooltips.mixin.client.ArmorTrimMixin",
            name = "appendTrimTooltip"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("TAIL"))
    private static void addEffectTooltip(List<Text> tooltip, ArmorTrim armorTrim, CallbackInfo ci) {
        if (Screen.hasShiftDown()) {
            ArmorTrimEffects.getEffects(armorTrim).forEach(effect -> tooltip.add(
                   effect.getTooltip()
                         .copy()
                         .fillStyle(armorTrim.getMaterial()
                                             .value()
                                             .description()
                                             .getStyle()
                         )
            ));
        }
    }
}
