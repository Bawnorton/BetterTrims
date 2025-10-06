package com.bawnorton.bettertrims.client.tooltip.element;

import com.bawnorton.bettertrims.property.element.TrimElement;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;

public interface TrimElementTooltipProvider<T extends TrimElement> {
	TrimElementTooltipProvider<TrimElement> EMPTY = (level, element, includeCount) -> null;

	@Nullable ClientTooltipComponent getTooltip(ClientLevel level, T element, boolean includeCount);
}
