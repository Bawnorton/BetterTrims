package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import java.util.List;

public record CompositeTooltipComponent(List<ClientTooltipComponent> components, int gap) implements ClientTooltipComponent {
    @Override
    public int getHeight(Font font) {
        int maxHeight = 0;
        for (ClientTooltipComponent component : components) {
            if (component.getHeight(font) > maxHeight) {
                maxHeight = component.getHeight(font);
            }
        }
        return maxHeight;
    }

    @Override
    public int getWidth(Font font) {
        int totalWidth = 0;
        for (ClientTooltipComponent component : components) {
            totalWidth += component.getWidth(font) + gap;
        }
        if (!components.isEmpty()) {
            totalWidth -= gap;
        }
        return totalWidth;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        int currentX = x;
        for (ClientTooltipComponent component : components) {
            int componentWidth = component.getWidth(font);
            component.renderImage(font, currentX, y, componentWidth, height, graphics);
            currentX += componentWidth + gap;
        }
    }

    @Override
    public void renderText(GuiGraphics graphics, Font font, int x, int y) {
        int currentX = x;
        int height = getHeight(font);
        for (ClientTooltipComponent component : components) {
            int componentWidth = component.getWidth(font);
            int componentHeight = component.getHeight(font);
            int adjustedY = y + (height - componentHeight) / 2;
            component.renderText(graphics, font, currentX, adjustedY);
            currentX += componentWidth + gap;
        }
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        for (ClientTooltipComponent component : components) {
            if (component.showTooltipWithItemInHand()) {
                return true;
            }
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ClientTooltipComponent> components = new java.util.ArrayList<>();
        private int gap = 2;

        public Builder component(ClientTooltipComponent component) {
            components.add(component);
            return this;
        }

        public Builder translate(String key) {
            return component(new ClientTextTooltip(Component.translatable(key).getVisualOrderText()));
        }

        public Builder literal(String text) {
            return component(new ClientTextTooltip(Component.literal(text).getVisualOrderText()));
        }

        public Builder cycle(List<Item> items) {
            return component(new CyclingItemsTooltipComponent(items));
        }

        public Builder gap(int gap) {
            this.gap = gap;
            return this;
        }

        public CompositeTooltipComponent build() {
            return new CompositeTooltipComponent(components, gap);
        }
    }
}
