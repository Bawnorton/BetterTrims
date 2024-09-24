package com.bawnorton.bettertrims.client.compat.yacl;

import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public final class ItemImageRenderer implements ImageRenderer {
    private final ItemStack stack;

    public ItemImageRenderer(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int render(DrawContext graphics, int x, int y, int renderWidth, float tickDelta) {
        float ratio = renderWidth / 16f;
        int targetHeight = (int) (16f * ratio);

        graphics.getMatrices().push();
        graphics.getMatrices().translate(x, y, 0);
        graphics.getMatrices().scale(ratio, ratio, 1);
        graphics.drawItem(stack, 0, 0);
        graphics.getMatrices().pop();

        return targetHeight;
    }

    @Override
    public void close() {
    }
}
