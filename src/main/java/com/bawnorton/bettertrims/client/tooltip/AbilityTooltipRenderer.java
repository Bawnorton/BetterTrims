package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.tooltip.braid.BraidAbilityTooltipRenderer;
import com.bawnorton.bettertrims.client.tooltip.vanilla.ExpandedTooltipPositioner;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.version.VGuiGraphics;
import com.mojang.blaze3d.platform.MacosUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AbilityTooltipRenderer {
	AbilityTooltipRenderer INSTANCE = /*? if braid {*/BraidAbilityTooltipRenderer.INSTANCE;/*?} else {*//*VanillaAbilityTooltipRenderer.INSTANCE;*//*?}*/
	ThreadLocal<ItemStack> STACK_CAPTURE = ThreadLocal.withInitial(() -> ItemStack.EMPTY);

	static void setStack(ItemStack stack) {
		STACK_CAPTURE.set(stack);
	}

	static ItemStack getStack() {
		return STACK_CAPTURE.get();
	}

	static void clearStack() {
		STACK_CAPTURE.remove();
	}

	default boolean mouseScrolled(double scrollY) {
		return false;
	}

	default void clearRendering() {

	}

	void render(GuiGraphics graphics, ItemStack stack, Font font, Rect2i tooltipBounds, int mouseX, @Nullable ResourceLocation background);

	default boolean hasNoProperties(ClientLevel level, ArmorTrim trim) {
		for (TrimProperty property : TrimProperties.getProperties(level)) {
			if (property.matcher().matches(trim)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("DataFlowIssue")
	default void renderPrompt(GuiGraphics graphics, Font font, Rect2i tooltipBounds, boolean horizontallyFlipped, @Nullable ResourceLocation background) {
		int promptWidth = 8;
		int promptHeight = 8;
		int xOffset = tooltipBounds.getX() + (horizontallyFlipped ? -promptWidth - 10 : tooltipBounds.getWidth() + 10);
		int yOffset = tooltipBounds.getY();
		if (MacosUtil.IS_MACOS) {
			//? if >=1.21.8 {
			TooltipRenderUtil.renderTooltipBackground(graphics, xOffset, yOffset, promptWidth, promptHeight, background);
			graphics.blitSprite(
					RenderPipelines.GUI_TEXTURED,
					BetterTrims.rl("text/alt"),
					xOffset,
					yOffset,
					promptWidth,
					promptHeight,
					ARGB.color(0xFF, ChatFormatting.GOLD.getColor())
			);
			//?} else {
			/*graphics.pose().pushPose();
			graphics.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(graphics, xOffset, yOffset, promptWidth, promptHeight, 400));
			graphics.pose().translate(0, 0, 400);
			int colour = ChatFormatting.GOLD.getColor();
			float[] existing = new float[4];
			for (int i = 0; i < 4; i++) {
				existing[i] = RenderSystem.getShaderColor()[i];
			}
			RenderSystem.setShaderColor(
					FastColor.ARGB32.red(colour) / 255f,
					FastColor.ARGB32.green(colour) / 255f,
					FastColor.ARGB32.blue(colour) / 255f,
					1f
			);
			graphics.blitSprite(
					BetterTrims.rl("text/alt"),
					xOffset,
					yOffset,
					promptWidth,
					promptHeight
			);
			RenderSystem.setShaderColor(existing[0], existing[1], existing[2], existing[3]);
			graphics.pose().popPose();
			*///?}
		} else {
			VGuiGraphics.renderTooltip(
					graphics,
					font,
					List.of(new ClientTextTooltip(Component.literal("Alt").withStyle(Styler::trim).getVisualOrderText())),
					xOffset - 10,
					yOffset,
					new ExpandedTooltipPositioner(tooltipBounds.getWidth()),
					background
			);
		}
	}
}
