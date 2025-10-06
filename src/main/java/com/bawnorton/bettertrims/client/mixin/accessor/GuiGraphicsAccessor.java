//? if <1.21.8 {
/*package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@MixinEnvironment("client")
@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
	@Invoker("renderTooltipInternal")
	void bettertrims$renderTooltipInternal(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner);
}
*///?}