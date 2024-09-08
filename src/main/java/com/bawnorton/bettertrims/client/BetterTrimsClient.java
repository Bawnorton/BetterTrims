package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.keybind.KeybindManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class BetterTrimsClient {
    public static void init() {
        BetterTrims.LOGGER.debug( "{} Client Initialized", BetterTrims.MOD_ID);

        ClientNetworking.init();
        KeybindManager.init();
    }

    public static ImageRenderer getImage(float value) {
        return new ImageRenderer() {
            @Override
            public int render(DrawContext context, int x, int y, int width, float tickDelta) {
                context.drawItem(Items.EMERALD.getDefaultStack(), x, (int) (y + value * 30));
                return (int) (y + value * 30 + 16);
            }

            @Override
            public void close() {

            }
        };
    }
}
