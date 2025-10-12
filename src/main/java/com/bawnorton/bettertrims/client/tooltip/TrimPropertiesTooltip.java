package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.client.tooltip.component.GapComponent;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.version.VGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrimPropertiesTooltip {
	private final Map<Matcher, List<TrimProperty>> properties;
	private final List<TrimTooltipPage> pages;

	private int index;

	public TrimPropertiesTooltip(Map<Matcher, List<TrimProperty>> properties) {
		this.properties = properties;
		this.pages = new ArrayList<>();
	}

	public void generatePages(ClientLevel level, Font font) {
		pages.clear();

		for (Map.Entry<Matcher, List<TrimProperty>> entry : properties.entrySet()) {
			Matcher matcher = entry.getKey();
			List<TrimProperty> properties = entry.getValue();
			for (TrimProperty property : properties) {
				TrimTooltipPage page = new TrimTooltipPage(property, matcher);
				pages.add(page);
			}
		}
		for (int i = 0; i < pages.size(); i++) {
			pages.get(i).generateComponent(level, font, i, pages.size());
		}
	}

	public void render(GuiGraphics graphics, ClientLevel level, Font font, Rect2i parentTooltipBounds, boolean horizontallyFlipped, ResourceLocation background) {
		if (index >= pages.size()) return;

		TrimTooltipPage page = pages.get(index);
		List<ClientTooltipComponent> components = new ArrayList<>();
		components.add(page.getComponent());
		components.add(new GapComponent(page.getRenderedWidth(font), 0));

		int offsetX = parentTooltipBounds.getX() + parentTooltipBounds.getWidth() + (horizontallyFlipped ? 14 : 0);
		int offsetY = parentTooltipBounds.getY();

		VGuiGraphics.renderTooltip(
				graphics,
				font,
				components,
				offsetX,
				offsetY,
				new ExpandedTooltipPositioner(parentTooltipBounds.getWidth()),
				background
		);
	}

	public boolean mouseScrolled(double scrollY) {
		if (pages.size() <= 1) return false;
		if (scrollY > 0) {
			index--;
			if (index < 0) index = pages.size() - 1;
		} else if (scrollY < 0) {
			index++;
			if (index >= pages.size()) index = 0;
		} else {
			return false;
		}
		return true;
	}
}
