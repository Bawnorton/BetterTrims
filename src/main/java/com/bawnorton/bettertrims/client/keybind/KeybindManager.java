package com.bawnorton.bettertrims.client.keybind;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.networking.packet.c2s.MagnetToggleC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class KeybindManager {
    private static final List<ActionedKeybind> KEYBINDS = new ArrayList<>();
    public static final ActionedKeybind TOGGLE_MAGNET = registerKeybind("key.bettertrims.toggle_magnet", GLFW.GLFW_KEY_H, client -> {
        ClientPlayerEntity player = client.player;
        if(player == null) return;

        boolean enabled = TrimEffects.IRON.isEnabledFor(player);
        TrimEffects.IRON.setEnabled(player, !enabled);
        ClientPlayNetworking.send(new MagnetToggleC2SPacket(TrimEffects.IRON.isEnabledFor(player)));
    });

    private static ActionedKeybind registerKeybind(String key, int code, KeybindCallback callback) {
        ActionedKeybind keybind = new ActionedKeybind(KeyBindingHelper.registerKeyBinding(new KeyBinding(
                key,
                InputUtil.Type.KEYSYM,
                code,
                "key.categories.bettertrims"
        )), callback);
        KEYBINDS.add(keybind);
        return keybind;
    }

    public static void runKeybindActions(MinecraftClient client) {
        KEYBINDS.forEach(keybind -> keybind.runIfPressed(client));
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(KeybindManager::runKeybindActions);
    }

    @FunctionalInterface
    public interface KeybindCallback {
        void onKeybindPressed(MinecraftClient client);
    }

    public static class ActionedKeybind {
        private final KeyBinding keybind;
        private final KeybindCallback callback;

        public ActionedKeybind(KeyBinding keybind, KeybindCallback callback) {
            this.keybind = keybind;
            this.callback = callback;
        }

        public KeyBinding getKeybind() {
            return keybind;
        }

        public void runIfPressed(MinecraftClient client) {
            while (keybind.wasPressed()) {
                run(client);
            }
        }

        public void run(MinecraftClient client) {
            callback.onKeybindPressed(client);
        }
    }
}
