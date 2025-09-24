package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemComponent(ItemStack stack) implements ClientTooltipComponent {
    @Override
    public int getHeight(@NotNull Font font) {
        return 16;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 16;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphics graphics) {
        graphics.renderFakeItem(stack, x, y - 2);
    }

    @Override
    public @NotNull String toString() {
        return "Item{%s}".formatted(stack);
    }
}
