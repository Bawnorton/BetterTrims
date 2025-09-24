package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment("client")
@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin {
    @Inject(
        method = "mouseScrolled",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        if(AbilityTooltipRenderer.mouseScrolled(scrollY)) {
            cir.setReturnValue(true);
        }
    }
}
