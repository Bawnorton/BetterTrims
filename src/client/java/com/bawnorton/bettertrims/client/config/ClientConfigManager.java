package com.bawnorton.bettertrims.client.config;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.impl.YACLImpl;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.compat.Compat;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.net.URI;

public abstract class ClientConfigManager {
    public static Config getConfig() {
        if (!ConfigManager.loaded()) {
            BetterTrims.LOGGER.warn("Attempted to access configs before they were loaded, loading configs now");
            ConfigManager.loadConfigs();
        }
        if (ClientNetworking.isConnectedToDedicated()) return Config.getServerInstance();
        return Config.getLocalInstance();
    }

    public static Screen getConfigScreen(Screen parent) {
        if (Compat.isYaclLoaded()) {
            return YACLImpl.getScreen(parent);
        } else {
            return new ConfirmScreen((result) -> {
                if (result) {
                    Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/yacl/versions"));
                }
                MinecraftClient.getInstance().setScreen(parent);
            }, Text.translatable("bettertrims.yacl.not_installed"), Text.translatable("bettertrims.yacl.not_installed.desc"), ScreenTexts.YES, ScreenTexts.NO);
        }
    }
}
