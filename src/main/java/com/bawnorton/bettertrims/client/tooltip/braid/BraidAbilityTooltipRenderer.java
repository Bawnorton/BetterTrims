package com.bawnorton.bettertrims.client.tooltip.braid;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import com.bawnorton.bettertrims.client.tooltip.braid.screen.BackgroundlessBraidScreen;
import com.bawnorton.bettertrims.client.tooltip.braid.widget.TrimPropertiesRootWidget;
import io.wispforest.owo.braid.core.BraidScreen;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.OwoUIPipelines;
import io.wispforest.owo.ui.renderstate.CircleElementRenderState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class BraidAbilityTooltipRenderer implements AbilityTooltipRenderer {
	public static final AbilityTooltipRenderer INSTANCE = new BraidAbilityTooltipRenderer();

	private int altDownFrames = 0;

	@Override
	public void render(GuiGraphics graphics, ItemStack stack, Font font, Rect2i tooltipBounds, int mouseX, @Nullable ResourceLocation background) {
		if(stack.isEmpty()) return;

		ArmorTrim trim = stack.get(DataComponents.TRIM);
		if (trim == null) return;

		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) return;
		if (hasNoProperties(level, trim)) return;

		boolean hasAltDown = minecraft.hasAltDown();
		if(hasAltDown) {
			altDownFrames++;
		} else {
			altDownFrames = 0;
		}

		if(altDownFrames > minecraft.getFps() * 0.6) {
			openTrimScreen(minecraft, stack, trim);
		} else {
			boolean shouldFlip = mouseX + tooltipBounds.getWidth() + 38 > graphics.guiWidth();
			if (hasAltDown) {
				renderPromptWithProgress(graphics, tooltipBounds, shouldFlip, background, altDownFrames / (float) (minecraft.getFps() / 2));
			} else {
				renderPrompt(graphics, font, tooltipBounds, shouldFlip, background);
			}
		}
	}

	@SuppressWarnings("DataFlowIssue")
	private void renderPromptWithProgress(GuiGraphics graphics, Rect2i tooltipBounds, boolean horizontallyFlipped, @Nullable ResourceLocation background, float progress) {
		int promptWidth = 8;
		int promptHeight = 8;
		int xOffset = tooltipBounds.getX() + (horizontallyFlipped ? -promptWidth - 10 : tooltipBounds.getWidth() + 10);
		int yOffset = tooltipBounds.getY();
		TooltipRenderUtil.renderTooltipBackground(graphics, xOffset, yOffset, promptWidth, promptHeight, background);
		int segments = 16;
		int radius = promptWidth / 2;
		int centerX = xOffset + radius;
		int centerY = yOffset + radius;
		double angleFrom = 90;
		double angleTo = angleFrom + progress * 360;
		graphics.guiRenderState.submitGuiElement(new CircleElementRenderState(
				OwoUIPipelines.GUI_TRIANGLE_FAN,
				new Matrix3x2f(graphics.pose()),
				graphics.scissorStack.peek(),
				centerX,
				centerY,
				angleFrom,
				angleTo,
				segments,
				radius,
				Color.ofRgb(ChatFormatting.GOLD.getColor())
		));
	}

	private void openTrimScreen(Minecraft minecraft, ItemStack stack, ArmorTrim trim) {
		BraidScreen.Settings settings = new BraidScreen.Settings();
		settings.shouldPause = false;
		settings.useBraidAppWidget = false;

		minecraft.setScreen(new BackgroundlessBraidScreen(settings, new TrimPropertiesRootWidget(stack, trim)));
	}
}
