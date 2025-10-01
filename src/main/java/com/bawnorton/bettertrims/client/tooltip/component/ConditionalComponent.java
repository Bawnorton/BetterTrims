package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import java.util.function.Supplier;

public record ConditionalComponent(ClientTooltipComponent component, Supplier<Boolean> checker) implements DynamicWidthComponent {
    @Override
    public int getHeight(Font font) {
        return checker.get() ? component.getHeight(font) : 0;
    }

    @Override
    public int getWidth(Font font) {
        return checker.get() ? component.getWidth(font) : 0;
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Font font, int x, int y) {
        if (checker.get()) {
            component.renderText(guiGraphics, font, x, y);
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        if (checker.get()) {
            component.renderImage(font, x, y, width, height, guiGraphics);
        }
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return checker.get() && component.showTooltipWithItemInHand();
    }

    @Override
    public int getMaxWidth(Font font) {
        if (!checker.get()) return 0;

        if (component instanceof DynamicWidthComponent dynamic) {
            return dynamic.getMaxWidth(font);
        }
        return component.getWidth(font);
    }
}
