package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.client.tooltip.component.GapComponent;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperty;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Properties {Material Provider Icon - Pattern Provider Icon} - [1/n]
 * Any of [Slots]
 * {Ability Icon} {Name} - [Conditions]
 * ...
 * N of [Slots]
 * {Ability Icon} {Name} - [Conditions]
 * ...
 * As Item
 * {Property Icon} {Name} - [Conditions]
 * ...
 */
public class TrimPropertiesTooltip {
    private final ArmorTrim trim;
    private final Map<Matcher, List<TrimProperty>> properties;
    private final List<TrimTooltipPage> pages;

    private int index;

    public TrimPropertiesTooltip(ArmorTrim trim, Map<Matcher, List<TrimProperty>> properties) {
        this.trim = trim;
        this.properties = properties;
        this.pages = new ArrayList<>();
    }

    public void generatePages(ClientLevel level, Font font) {
        pages.clear();

        for (Map.Entry<Matcher, List<TrimProperty>> entry : properties.entrySet()) {
            Matcher matcher = entry.getKey();
            List<TrimProperty> properties = entry.getValue();
            for (int i = 0; i < properties.size(); i++) {
                TrimTooltipPage page = new TrimTooltipPage(properties.get(i), matcher);
                page.generateComponent(level, trim, font, i, properties.size());
                pages.add(page);
            }
        }
    }

    public void render(GuiGraphics graphics, ClientLevel level, Font font, Rect2i parentTooltipBounds, boolean horizontallyFlipped, ResourceLocation background) {
        if (index >= pages.size()) return;

        TrimTooltipPage page = pages.get(index);
        List<ClientTooltipComponent> components = new ArrayList<>();
        components.add(page.getComponent());
        components.add(new GapComponent(page.getMaxWidth(font), 0));

        int offsetX = parentTooltipBounds.getX() + parentTooltipBounds.getWidth() + (horizontallyFlipped ? 14 : 0);
        int offsetY = parentTooltipBounds.getY();

        graphics.renderTooltip(
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
