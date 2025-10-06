package com.bawnorton.bettertrims.version;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

//? if <1.21.8 {
/*import com.bawnorton.bettertrims.client.mixin.accessor.GuiGraphicsAccessor;
*///?}

public interface VGuiGraphics {
	static void renderTooltip(GuiGraphics graphics, Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner, ResourceLocation background) {
		//? if >=1.21.8 {
		graphics.renderTooltip(font, components, mouseX, mouseY, tooltipPositioner, background);
		 //?} else {
		/*graphics.pose().pushPose();
		graphics.pose().translate(0, 0, 1000);
		((GuiGraphicsAccessor) graphics).bettertrims$renderTooltipInternal(font, components, mouseX, mouseY, tooltipPositioner);
		graphics.pose().popPose();
		*///?}
	}
}
