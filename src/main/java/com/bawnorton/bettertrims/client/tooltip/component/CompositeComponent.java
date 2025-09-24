package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import java.util.List;

public interface CompositeComponent {
    List<ClientTooltipComponent> getComponents();

    default int getMaxWidth(Font font) {
        int maxWidth = 0;
        for (ClientTooltipComponent component : getComponents()) {
            if (component instanceof CompositeComponent compositeComponent) {
                int width = compositeComponent.getMaxWidth(font);
                if (width > maxWidth) {
                    maxWidth = width;
                }
            } else {
                int width = component.getWidth(font);
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }
        return maxWidth;
    }
}
