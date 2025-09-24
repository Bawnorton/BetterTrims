package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public record StackedItemsComponent(List<ItemStack> items, int offset) implements ClientTooltipComponent {
    public StackedItemsComponent(List<ItemStack> items, int offset) {
        this.items = items.reversed();
        this.offset = offset;
    }

    @Override
    public int getHeight(Font font) {
        return 16;
    }

    @Override
    public int getWidth(Font font) {
        return Math.min(16, (16 - offset) + offset * items.size());
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        if (items.isEmpty()) return;

        int currentOffset = items.size() - 1;
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;

            guiGraphics.renderFakeItem(stack, x + currentOffset * offset, y - 2);
            currentOffset--;
        }
    }

    @Override
    public @NotNull String toString() {
        return "StackedItems{%s}".formatted(items);
    }
}
