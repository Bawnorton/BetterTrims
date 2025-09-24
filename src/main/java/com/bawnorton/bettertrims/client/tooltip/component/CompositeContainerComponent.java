package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public abstract class CompositeContainerComponent implements ClientTooltipComponent, CompositeComponent {
    protected final List<ClientTooltipComponent> components;
    protected final boolean centred;

    protected CompositeContainerComponent(List<ClientTooltipComponent> components, boolean centred) {
        this.components = components;
        this.centred = centred;
    }

    @Override
    public List<ClientTooltipComponent> getComponents() {
        return components;
    }

    public boolean isEmpty() {
        if(components.isEmpty()) return true;

        for (ClientTooltipComponent component : components) {
            if (!(component instanceof CompositeContainerComponent composite)) {
                return false;
            }
            if (!composite.isEmpty()) {
                return false;
            }
        }
        return true;
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

    @Override
    public String toString() {
        return "Composite{%s, centred=%s}".formatted(components, centred);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends CompositeBuilder<CompositeContainerComponent> {
        private boolean spaced = false;
        private boolean vertical = false;
        private boolean centred = false;

        public Builder component(ClientTooltipComponent component) {
            return (Builder) super.component(component);
        }

        public Builder textComponent(Component component) {
            return (Builder) super.textComponent(component);
        }

        public Builder translate(String key, Object... args) {
            return (Builder) super.translate(key, args);
        }

        public Builder translate(String key, UnaryOperator<Style> styler, Object... args) {
            return (Builder) super.translate(key, styler, args);
        }

        public Builder literal(String text) {
            return (Builder) super.literal(text);
        }

        public Builder literal(String text, UnaryOperator<Style> styler) {
            return (Builder) super.literal(text, styler);
        }

        public Builder space() {
            return (Builder) super.space();
        }

        public Builder cycle(Consumer<CyclingComponent.Builder> consumer) {
            CyclingComponent.Builder builder = CyclingComponent.builder();
            consumer.accept(builder);
            CyclingComponent cycling = builder.build();
            return switch (cycling.size()) {
                case 0 -> this;
                case 1 -> component(cycling.get(0));
                default -> component(cycling);
            };
        }

        public Builder stack(List<ItemStack> items, int offset) {
            return component(new StackedItemsComponent(items, offset));
        }

        public Builder spaced() {
            if (spaced) return this;

            spaced = true;
            return this;
        }

        public Builder vertical() {
            if (vertical) return this;

            vertical = true;
            return this;
        }

        public Builder centred() {
            if (centred) return this;

            centred = true;
            return this;
        }

        public CompositeContainerComponent build() {
            if (!spaced) {
                if(vertical) {
                    return new Vertical(components, centred);
                } else {
                    return new Horizontal(components, centred);
                }
            }

            List<ClientTooltipComponent> spacedComponents = new ArrayList<>();
            for (int i = 0; i < components.size(); i++) {
                spacedComponents.add(components.get(i));
                if (i < components.size() - 1) {
                    spacedComponents.add(new ClientTextTooltip(Component.literal(" ").getVisualOrderText()));
                }
            }
            if(vertical) {
                return new Vertical(spacedComponents, centred);
            } else {
                return new Horizontal(spacedComponents, centred);
            }
        }
    }

    public static final class Horizontal extends CompositeContainerComponent {
        private Horizontal(List<ClientTooltipComponent> components, boolean centred) {
            super(components, centred);
        }

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
                totalWidth += component.getWidth(font);
            }
            return totalWidth;
        }

        @Override
        public int getMaxWidth(Font font) {
            int totalWidth = 0;
            for (ClientTooltipComponent component : components) {
                if (component instanceof CompositeComponent compositeComponent) {
                    totalWidth += compositeComponent.getMaxWidth(font);
                } else {
                    totalWidth += component.getWidth(font);
                }
            }
            return totalWidth;
        }

        @Override
        public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
            int currentX = x;
            for (ClientTooltipComponent component : components) {
                int componentWidth = component.getWidth(font);
                component.renderImage(font, currentX, y, componentWidth, height, graphics);
                currentX += componentWidth;
            }
        }

        @Override
        public void renderText(GuiGraphics graphics, Font font, int x, int y) {
            int currentX = x;
            int height = getHeight(font);
            for (ClientTooltipComponent component : components) {
                int componentWidth = component.getWidth(font);
                if(centred) {
                    int componentHeight = component.getHeight(font);
                    component.renderText(graphics, font, currentX, y + (height - componentHeight) / 2);
                } else {
                    component.renderText(graphics, font, currentX, y);
                }
                currentX += componentWidth;
            }
        }
    }

    public static final class Vertical extends CompositeContainerComponent {
        private Vertical(List<ClientTooltipComponent> components, boolean centred) {
            super(components, centred);
        }

        @Override
        public int getHeight(Font font) {
            int totalHeight = 0;
            for (ClientTooltipComponent component : components) {
                totalHeight += component.getHeight(font);
            }
            return totalHeight;
        }

        @Override
        public int getWidth(Font font) {
            int maxWidth = 0;
            for (ClientTooltipComponent component : components) {
                if (component.getWidth(font) > maxWidth) {
                    maxWidth = component.getWidth(font);
                }
            }
            return maxWidth;
        }

        @Override
        public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
            int currentY = y;
            for (ClientTooltipComponent component : components) {
                int componentHeight = component.getHeight(font);
                component.renderImage(font, x, currentY, width, componentHeight, graphics);
                currentY += componentHeight;
            }
        }

        @Override
        public void renderText(GuiGraphics graphics, Font font, int x, int y) {
            int currentY = y;
            for (ClientTooltipComponent component : components) {
                int componentHeight = component.getHeight(font);
                component.renderText(graphics, font, x, currentY);
                currentY += componentHeight;
            }
        }
    }
}
