package com.bawnorton.bettertrims.client.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public record ExpandedTooltipPositioner(int parentTooltipWidth) implements ClientTooltipPositioner {
    @Override
    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        Vector2i tooltipPos = new Vector2i(mouseX, mouseY).add(10, 0);
        if (tooltipPos.x + tooltipWidth > screenWidth) {
            tooltipPos.x = Math.max(tooltipPos.x - tooltipWidth - parentTooltipWidth - 34, 4);
        }

        int i = tooltipHeight + 3;
        if (tooltipPos.y + i > screenHeight) {
            tooltipPos.y = screenHeight - i;
        }
        return tooltipPos;
    }
}
