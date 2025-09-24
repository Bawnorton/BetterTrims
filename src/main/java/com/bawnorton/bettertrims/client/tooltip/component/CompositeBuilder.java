package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public abstract class CompositeBuilder<T> {
    protected final List<ClientTooltipComponent> components = new ArrayList<>();

    public CompositeBuilder<T> component(ClientTooltipComponent component) {
        components.add(component);
        return this;
    }

    public CompositeBuilder<T> textComponent(Component component) {
        return component(new ClientTextTooltip(component.getVisualOrderText()));
    }

    public CompositeBuilder<T> translate(String key, Object... args) {
        return textComponent(Component.translatable(key, args));
    }

    public CompositeBuilder<T> translate(String key, UnaryOperator<Style> styler, Object... args) {
        return textComponent(Component.translatable(key, args).withStyle(styler));
    }

    public CompositeBuilder<T> literal(String text) {
        return textComponent(Component.literal(text));
    }

    public CompositeBuilder<T> literal(String text, UnaryOperator<Style> styler) {
        return textComponent(Component.literal(text).withStyle(styler));
    }

    public CompositeBuilder<T> space() {
        return literal(" ");
    }

    public abstract T build();
}
