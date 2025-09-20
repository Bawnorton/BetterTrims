package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CyclingItemsTooltipComponent implements ClientTooltipComponent {
    private final List<Item> items;

    private int index = 0;
    private int frameCounter = 0;

    public CyclingItemsTooltipComponent(List<Item> items) {
        this.items = items;
    }

    public static CyclingItemsTooltipComponent of(Item... items) {
        return new CyclingItemsTooltipComponent(List.of(items));
    }

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
        if(items.isEmpty()) return;

        if(frameCounter++ >= Minecraft.getInstance().getFps()) {
            frameCounter = 0;
            index = (index + 1) % items.size();
        }

        ItemStack stack = items.get(index).getDefaultInstance();
        graphics.renderFakeItem(stack, x, y - 2);
    }
}
