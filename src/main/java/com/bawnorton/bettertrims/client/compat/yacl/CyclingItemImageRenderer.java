package com.bawnorton.bettertrims.client.compat.yacl;

import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public final class CyclingItemImageRenderer implements ImageRenderer {
    private final List<ItemImageRenderer> renderers = new ArrayList<>();
    private int index = 0;
    private int count = 0;

    public CyclingItemImageRenderer(List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            ItemImageRenderer renderer = new ItemImageRenderer(stack);
            renderers.add(renderer);
        }
    }

    @Override
    public int render(DrawContext graphics, int x, int y, int renderWidth, float tickDelta) {
        if(renderers.isEmpty()) return 0;

        count++;
        if(MinecraftClient.getInstance().getCurrentFps() >= count) {
            count = 0;
            index++;
            if(index >= renderers.size()) {
                index = 0;
            }
        }
        return renderers.get(index).render(graphics, x, y, renderWidth, tickDelta);
    }

    @Override
    public void close() {
    }
}
