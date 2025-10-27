package com.bawnorton.bettertrims.client.tooltip.vanilla.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

public record GapComponent(int width, int height) implements ClientTooltipComponent {
	@Override
	//? if >=1.21.8 {
	public int getHeight(Font font) {
		//?} else {
		/*public int getHeight() {
		 *///?}
		return height;
	}

	@Override
	public int getWidth(Font font) {
		return width;
	}

	@Override
	public @NotNull String toString() {
		return "Gap{%d, %d}".formatted(width, height);
	}
}
