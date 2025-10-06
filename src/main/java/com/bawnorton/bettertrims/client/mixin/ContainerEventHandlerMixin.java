package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment("client")
@Mixin(ContainerEventHandler.class)
interface ContainerEventHandlerMixin {
	@Inject(
			method = "mouseScrolled",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
		if (AbilityTooltipRenderer.mouseScrolled(scrollY)) {
			cir.setReturnValue(true);
		}
	}
}
