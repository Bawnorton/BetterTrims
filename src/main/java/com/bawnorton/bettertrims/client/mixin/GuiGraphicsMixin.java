package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@MixinEnvironment("client")
@Mixin(GuiGraphics.class)
abstract class GuiGraphicsMixin {
	@Unique
	private static final ThreadLocal<Rect2i> TOOLTIP_DIMENSION_CAPTURE = new ThreadLocal<>();

	 //? if >=1.21.8 {
    @Definition(id = "deferredTooltip", field = "Lnet/minecraft/client/gui/GuiGraphics;deferredTooltip:Ljava/lang/Runnable;")
    @Expression("this.deferredTooltip = @(?)")
    @ModifyExpressionValue(
        method = "setTooltipForNextFrameInternal",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private Runnable renderTrimTooltip(
        Runnable original,
        Font font,
        List<ClientTooltipComponent> components,
        int x,
        int y,
        ClientTooltipPositioner positioner,
        @Nullable ResourceLocation background,
        boolean focused
    ) {
        ItemStack stack = AbilityTooltipRenderer.getStack();
        if (stack.isEmpty()) {
            AbilityTooltipRenderer.clearRendering();
            return original;
        }

        GuiGraphics self = (GuiGraphics) (Object) this;
        AbilityTooltipRenderer.clearStack();
        return () -> {
            original.run();
            AbilityTooltipRenderer.render(self, stack, font, TOOLTIP_DIMENSION_CAPTURE.get(), x, background);
            TOOLTIP_DIMENSION_CAPTURE.remove();
        };
    }
  //?} else {
	/*@Inject(
			method = "renderTooltipInternal",
			at = @At("TAIL")
	)
	*///?}
	private void renderTrimTooltip(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner, CallbackInfo ci) {
		ItemStack stack = AbilityTooltipRenderer.getStack();
		if (stack.isEmpty()) {
			return;
		}

		GuiGraphics self = (GuiGraphics) (Object) this;
		AbilityTooltipRenderer.clearStack();
		AbilityTooltipRenderer.render(self, stack, font, TOOLTIP_DIMENSION_CAPTURE.get(), mouseX, null);
		TOOLTIP_DIMENSION_CAPTURE.remove();
	}

	@WrapOperation(
			//? if >=1.21.8 {
			//? if fabric {
			method = "renderTooltip",
			//?} else {
			/*method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/ItemStack;)V",
			*///?}
			//?} else {
			/*method = "renderTooltipInternal",
			*///?}
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"
			)
	)
	private Vector2ic captureTooltipPosition(
			ClientTooltipPositioner instance,
			int screenWidth,
			int screenHeight,
			int mouseX,
			int mouseY,
			int tooltipWidth,
			int tooltipHeight,
			Operation<Vector2ic> original
	) {
		Vector2ic result = original.call(instance, screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
		TOOLTIP_DIMENSION_CAPTURE.set(new Rect2i(result.x(), result.y(), tooltipWidth, tooltipHeight));
		return result;
	}
}
