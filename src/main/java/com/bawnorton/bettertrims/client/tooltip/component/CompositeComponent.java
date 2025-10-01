package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import java.util.List;

public interface CompositeComponent extends DynamicWidthComponent {
    List<ClientTooltipComponent> getComponents();

    default int getMaxWidth(Font font) {
        int maxWidth = 0;
        for (ClientTooltipComponent component : getComponents()) {
            maxWidth = DynamicWidthComponent.getMaxWidth(font, component, maxWidth);
        }
        return maxWidth;
    }

    default boolean isOneLine() {
        for (ClientTooltipComponent component : getComponents()) {
            if (component instanceof CompositeComponent composite) {
                if(composite.isOneLine()) continue;

                return false;
            }
        }
        return true;
    }
}
