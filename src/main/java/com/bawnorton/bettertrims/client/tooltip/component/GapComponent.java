package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

public record GapComponent(int width, int height) implements ClientTooltipComponent {
    @Override
    public int getHeight(Font font) {
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
