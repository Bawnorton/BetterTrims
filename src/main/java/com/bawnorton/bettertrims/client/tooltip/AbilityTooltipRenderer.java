package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AbilityTooltipRenderer {
    private static final ThreadLocal<ItemStack> STACK_CAPTURE = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    private static final Map<ArmorTrim, TrimPropertiesTooltip> tooltips = new HashMap<>();
    private static TrimPropertiesTooltip currentTooltip = null;
    private static final LongSet EXCEPTION_HASHES = new LongArraySet();

    public static void render(GuiGraphics graphics, ItemStack stack, Font font, List<ClientTooltipComponent> components, Rect2i tooltipBounds, int mouseX, int mouseY, @Nullable ResourceLocation background) {
        if (stack.isEmpty()) return;

        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim == null) return;

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) return;

        if(!Screen.hasAltDown()) {
            boolean shouldFlip = mouseX + tooltipBounds.getWidth() + 38 > graphics.guiWidth();
            renderPrompt(graphics, tooltipBounds, shouldFlip, background);
            return;
        }

        boolean shouldFlip = mouseX > tooltipBounds.getX();
        try {
            renderProperties(graphics, level, font, trim, tooltipBounds, shouldFlip, background);
        } catch (Exception e) {
            long hash = e.getClass().getName().hashCode();
            hash ^= e.getMessage() != null ? e.getMessage().hashCode() : 0;
            for (StackTraceElement element : e.getStackTrace()) {
                hash ^= 31L * hash * element.getClassName().hashCode();
                hash ^= 31L * hash * element.getMethodName().hashCode();
                hash ^= 31L * hash * element.getLineNumber();
            }
            if(EXCEPTION_HASHES.add(hash)) {
                BetterTrims.LOGGER.error("Failed to render trim properties tooltip for trim {} on item {}", trim, stack, e);
            }
            renderError(graphics, font, tooltipBounds, shouldFlip, background);
        }
    }

    private static void renderProperties(GuiGraphics graphics, ClientLevel level, Font font, ArmorTrim trim, Rect2i tooltipBounds, boolean horizontallyFlipped, ResourceLocation background) {
        currentTooltip = tooltips.computeIfAbsent(trim, a -> {
            Map<Matcher, List<TrimProperty>> matchedProperties = new HashMap<>();
            for (TrimProperty property : TrimProperties.getProperties(level)) {
                Matcher matcher = property.matcher();
                if (matcher.matches(trim)) {
                    matchedProperties.computeIfAbsent(matcher, m -> new ArrayList<>()).add(property);
                }
            }

            TrimPropertiesTooltip tooltip = new TrimPropertiesTooltip(trim, matchedProperties);
            tooltip.generatePages(level, font);
            return tooltip;
        });
        currentTooltip.render(graphics, level, font, tooltipBounds, horizontallyFlipped, background);
    }

    private static void renderPrompt(GuiGraphics graphics, Rect2i tooltipBounds, boolean horizontallyFlipped, @Nullable ResourceLocation background) {
        int promptWidth = 8;
        int promptHeight = 8;
        int xOffset = tooltipBounds.getX() + (horizontallyFlipped ? - promptWidth - 10: tooltipBounds.getWidth() + 10);
        int yOffset = tooltipBounds.getY();
        TooltipRenderUtil.renderTooltipBackground(graphics, xOffset, yOffset, promptWidth, promptHeight, background);
        //noinspection DataFlowIssue
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            BetterTrims.rl("text/alt"),
            xOffset,
            yOffset,
            promptWidth,
            promptHeight,
            ARGB.color(0xFF, ChatFormatting.GOLD.getColor())
        );
    }

    private static void renderError(GuiGraphics graphics, Font font, Rect2i tooltipBounds, boolean shouldFlip, @Nullable ResourceLocation background) {
        List<ClientTooltipComponent> errorComponents = List.of(new ClientTextTooltip(Component.translatable("bettertrims.tooltip.properties.error").withStyle(ChatFormatting.RED).getVisualOrderText()));
        int offsetX = tooltipBounds.getX() + tooltipBounds.getWidth() + (shouldFlip ? 14 : 0);
        int offsetY = tooltipBounds.getY();

        graphics.renderTooltip(
            font,
            errorComponents,
            offsetX,
            offsetY,
            new ExpandedTooltipPositioner(tooltipBounds.getWidth()),
            background
        );
    }

    public static boolean mouseScrolled(double scrollY) {
        if (currentTooltip == null) return false;

        return currentTooltip.mouseScrolled(scrollY);
    }

    public static void clearRendering() {
        currentTooltip = null;
    }

    public static void setStack(ItemStack stack) {
        STACK_CAPTURE.set(stack);
    }

    public static ItemStack getStack() {
        return STACK_CAPTURE.get();
    }

    public static void clearStack() {
        STACK_CAPTURE.remove();
    }
}
