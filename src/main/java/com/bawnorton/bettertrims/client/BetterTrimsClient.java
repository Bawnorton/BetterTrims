package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.keybind.KeybindManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import java.net.URI;

public final class BetterTrimsClient {
    public static void init() {
        BetterTrims.LOGGER.debug( "{} Client Initialized", BetterTrims.MOD_ID);

        ClientNetworking.init();
        KeybindManager.init();

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if(!world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));

            ItemStack handStack = player.getStackInHand(hand);
            if(handStack.isOf(Items.BOOK)) {
                Text customName = handStack.get(DataComponentTypes.CUSTOM_NAME);
                if(customName != null) {
                    if(customName.getString().equals("BetterTrims Guidebook")) {
                        String link = "https://github.com/Bawnorton/BetterTrims/blob/stonecutter/README.md";
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.setScreen(new ConfirmLinkScreen(open -> {
                            if (open) Util.getOperatingSystem().open(link);
                            client.setScreen(null);
                        }, link, true));
                        return TypedActionResult.success(handStack);
                    }
                }
            }
            return TypedActionResult.pass(handStack);
        });
    }

    public static Text twoDpFormatter(float value) {
        return Text.literal(String.format("%,.2f", value).replaceAll("[\u00a0\u202F]", " "));
    }
}
