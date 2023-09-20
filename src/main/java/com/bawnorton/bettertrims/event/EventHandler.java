package com.bawnorton.bettertrims.event;

import com.bawnorton.bettertrims.networking.Networking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public abstract class EventHandler {
    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(Networking::setServer);
    }
}
