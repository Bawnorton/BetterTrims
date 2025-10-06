package com.bawnorton.bettertrims.client.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

import java.util.function.Supplier;

public record ConditionalComponent(ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse,
                                   Supplier<Boolean> checker) implements DynamicWidthComponent {
	@Override
			//? if >=1.21.8 {
    public int getHeight(Font font) {
        return checker.get() ? ifTrue.getHeight(font) : ifFalse.getHeight(font);
    }
    //?} else {
	/*public int getHeight() {
		return checker.get() ? ifTrue.getHeight() : ifFalse.getHeight();
	}
	*///?}

	@Override
	public int getWidth(Font font) {
		return checker.get() ? ifTrue.getWidth(font) : ifFalse.getWidth(font);
	}

	//? if >=1.21.8 {
    @Override
    public void renderText(GuiGraphics guiGraphics, Font font, int x, int y) {
        if (checker.get()) {
            ifTrue.renderText(guiGraphics, font, x, y);
        } else {
            ifFalse.renderText(guiGraphics, font, x, y);
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        if (checker.get()) {
            ifTrue.renderImage(font, x, y, width, height, guiGraphics);
        } else {
            ifFalse.renderImage(font, x, y, width, height, guiGraphics);
        }
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return checker.get() && ifTrue.showTooltipWithItemInHand() || !checker.get() && ifFalse.showTooltipWithItemInHand();
    }
    //?} else {
	/*@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		if (checker.get()) {
			ifTrue.renderImage(font, x, y, guiGraphics);
		} else {
			ifFalse.renderImage(font, x, y, guiGraphics);
		}
	}

	@Override
	public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
		if (checker.get()) {
			ifTrue.renderText(font, mouseX, mouseY, matrix, bufferSource);
		} else {
			ifFalse.renderText(font, mouseX, mouseY, matrix, bufferSource);
		}
	}
	*///?}

	@Override
	public int getMaxWidth(Font font) {
		int maxWidth = 0;
		if (ifTrue instanceof DynamicWidthComponent dynamic) {
			maxWidth = Math.max(maxWidth, dynamic.getMaxWidth(font));
		} else {
			maxWidth = Math.max(maxWidth, ifTrue.getWidth(font));
		}
		return maxWidth;
	}
}
