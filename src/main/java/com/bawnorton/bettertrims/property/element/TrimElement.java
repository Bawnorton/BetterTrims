package com.bawnorton.bettertrims.property.element;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;

public interface TrimElement {
	default @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
		return null;
	}

	default boolean usesCount() {
		return false;
	}
}
