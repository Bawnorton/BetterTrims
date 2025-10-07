package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public interface DynamicWidthComponent extends ClientTooltipComponent {
	int getMaxWidth(Font font);

	int getMinWidth(Font font);

	static int getMaxWidth(Font font, ClientTooltipComponent component, int currentMax) {
		if (component instanceof DynamicWidthComponent dynamic) {
			int width = dynamic.getMaxWidth(font);
			if (width > currentMax) {
				currentMax = width;
			}
		} else {
			int width = component.getWidth(font);
			if (width > currentMax) {
				currentMax = width;
			}
		}
		return currentMax;
	}

	static int getMinWidth(Font font, ClientTooltipComponent component, int currentMin) {
		if (component instanceof DynamicWidthComponent dynamic) {
			int width = dynamic.getMinWidth(font);
			if (width < currentMin) {
				currentMin = width;
			}
		} else {
			int width = component.getWidth(font);
			if (width < currentMin) {
				currentMin = width;
			}
		}
		return currentMin;
	}
}
