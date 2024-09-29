package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.compat.Compat;
import com.bawnorton.bettertrims.client.compat.lambdynlights.LambDynLightsCompat;
import com.bawnorton.bettertrims.client.compat.sodiumdynlights.SodiumDynLightsCompat;
import com.bawnorton.bettertrims.client.keybind.KeybindManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributeApplicator;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;

//? if >=1.21
/*import net.minecraft.component.DataComponentTypes;*/

public final class BetterTrimsClient {
    public static void init() {
        BetterTrims.LOGGER.debug( "{} Client Initialized", BetterTrims.MOD_ID);

        ClientNetworking.init();
        KeybindManager.init();

        Compat.getLambDynLightsCompat().ifPresent(LambDynLightsCompat::init);
        Compat.getSodiumDynLightsCompat().ifPresent(SodiumDynLightsCompat::init);

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if(!world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));

            ItemStack handStack = player.getStackInHand(hand);
            if(handStack.isOf(Items.BOOK)) {
                //? if >=1.21 {
                /*Text customName = handStack.get(DataComponentTypes.CUSTOM_NAME);
                *///?} else {
                Text customName = handStack.getName();
                //?}
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

        //? if <1.21 {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> TrimEntityAttributeApplicator.registryManager = handler.getRegistryManager());
        //?}
    }

    public static Text twoDpFormatter(float value) {
        return Text.literal(String.format("%,.2f", value).replaceAll("[\u00a0\u202F]", " "));
    }

    public static Text threeDpFormatter(float value) {
        return Text.literal(String.format("%,.3f", value).replaceAll("[\u00a0\u202F]", " "));
    }
}
