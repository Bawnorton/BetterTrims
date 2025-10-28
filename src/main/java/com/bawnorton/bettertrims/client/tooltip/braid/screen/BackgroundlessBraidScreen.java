package com.bawnorton.bettertrims.client.tooltip.braid.screen;

import io.wispforest.owo.braid.core.BraidScreen;
import io.wispforest.owo.braid.framework.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;

public class BackgroundlessBraidScreen extends BraidScreen {
	public BackgroundlessBraidScreen(Settings settings, Widget rootWidget) {
		super(settings, rootWidget);
	}

	@Override
	protected void renderBlurredBackground(GuiGraphics guiGraphics) {

	}
}
