package com.bawnorton.bettertrims.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.registry.Registry;

public interface PreRegistryFreezeCallback {
    Event<PreRegistryFreezeCallback> PRE = EventFactory.createArrayBacked(
            PreRegistryFreezeCallback.class,
            listeners -> registry -> {
                for(PreRegistryFreezeCallback listener : listeners) {
                    listener.beforeFreeze(registry);
                }
            });

    Event<PreRegistryFreezeCallback> POST = EventFactory.createArrayBacked(
            PreRegistryFreezeCallback.class,
            listeners -> registry -> {
                for(PreRegistryFreezeCallback listener : listeners) {
                    listener.beforeFreeze(registry);
                }
            });

    void beforeFreeze(Registry<?> registry);
}
