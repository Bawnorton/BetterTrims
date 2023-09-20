package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.event.EventHandler;
import com.bawnorton.bettertrims.client.impl.YACLImpl;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.compat.Compat;
import com.bawnorton.bettertrims.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class BetterTrimsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterTrims.MOD_ID);

    public static Screen getConfigScreen(Screen parent) {
        if (Compat.isYaclLoaded()) {
            return YACLImpl.getScreen(parent);
        } else {
            return new ConfirmScreen((result) -> {
                if (result) {
                    Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/yacl/versions"));
                }
                MinecraftClient.getInstance().setScreen(parent);
            }, Text.translatable("bettertrims.yacl.not_installed"), Text.of("bettertrims.yacl.not_installed.desc"), ScreenTexts.YES, ScreenTexts.NO);
        }
    }

    public static Config getConfig() {
        if (ClientNetworking.isConnectedToDedicated()) return Config.getServerInstance();
        return Config.getLocalInstance();
    }

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Initializing Better Trims Client");
        ClientNetworking.init();
        EventHandler.init();
    }
}