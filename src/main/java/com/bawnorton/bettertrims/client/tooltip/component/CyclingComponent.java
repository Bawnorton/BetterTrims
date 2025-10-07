package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.UnaryOperator;

public final class CyclingComponent implements CompositeComponent {
	private final List<ClientTooltipComponent> components;

	private int index = 0;
	private int frameCounter = 0;

	public CyclingComponent(List<ClientTooltipComponent> components) {
		this.components = components;
	}

	@Override
	public List<ClientTooltipComponent> getComponents() {
		return components;
	}

	@Override
	//? if >=1.21.8 {
  public int getHeight(Font font) {
        return components.get(index).getHeight(font);
    }
  //?} else {
	/*public int getHeight() {
		return components.get(index).getHeight();
	}
	*///?}

	@Override
	public int getWidth(@NotNull Font font) {
		return components.get(index).getWidth(font);
	}

	//? if >=1.21.8 {
  @Override
  public void renderText(GuiGraphics graphics, Font font, int x, int y) {
    if(components.isEmpty()) return;

    if(frameCounter++ >= Minecraft.getInstance().getFps()) {
      frameCounter = 0;
      index = (index + 1) % components.size();
    }

    components.get(index).renderText(graphics, font, x, y);
  }

  @Override
  public void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphics graphics) {
    if(components.isEmpty()) return;

    components.get(index).renderImage(font, x, y, width, height, graphics);
  }
  //?} else {
	/*@Override
	public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
		if (components.isEmpty()) return;

		if (frameCounter++ >= Minecraft.getInstance().getFps()) {
			frameCounter = 0;
			index = (index + 1) % components.size();
		}

		components.get(index).renderText(font, mouseX, mouseY, matrix, bufferSource);
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		if (components.isEmpty()) return;

		components.get(index).renderImage(font, x, y, guiGraphics);
	}
	*///?}

	public int size() {
		return components.size();
	}

	public ClientTooltipComponent get(int index) {
		return components.get(index);
	}

	@Override
	public String toString() {
		return "Cycling{%s}".formatted(components);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends CompositeBuilder<CyclingComponent> {
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

		public CyclingComponent build() {
			return new CyclingComponent(components);
		}
	}
}
