package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public record StackedComponent(List<? extends ClientTooltipComponent> components,
                               int offset) implements ClientTooltipComponent {
	public StackedComponent(List<? extends ClientTooltipComponent> components, int offset) {
		this.components = components.reversed();
		this.offset = offset;
	}

	@Override
			//? if >=1.21.8 {
	public int getHeight(Font font) {
	 //?} else {
	/*public int getHeight() {
		*///?}
		return 16;
	}

	@Override
	public int getWidth(Font font) {
		return Math.max(16, (16 - offset) + offset * components.size());
	}

	//? if >=1.21.8 {
    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        if (components.isEmpty()) return;

        int currentOffset = components.size() - 1;
        for (ClientTooltipComponent component : components) {
            component.renderImage(font, x + (offset * currentOffset), y, width, height, graphics);
            currentOffset--;
        }
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Font font, int x, int y) {
        if (components.isEmpty()) return;

        int currentOffset = components.size() - 1;
        for (ClientTooltipComponent component : components) {
            component.renderText(guiGraphics, font, x + (offset * currentOffset), y);
            currentOffset--;
        }
    }
    //?} else {

	/*@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		if (components.isEmpty()) return;

		int currentOffset = components.size() - 1;
		for (ClientTooltipComponent component : components) {
			component.renderImage(font, x + (offset * currentOffset), y, guiGraphics);
			currentOffset--;
		}
	}

	@Override
	public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
		if (components.isEmpty()) return;

		int currentOffset = components.size() - 1;
		for (ClientTooltipComponent component : components) {
			component.renderText(font, mouseX + (offset * currentOffset), mouseY, matrix, bufferSource);
			currentOffset--;
		}
	}

	*///?}

	@Override
	public @NotNull String toString() {
		return "StackedComponent{%s}".formatted(components);
	}
}
