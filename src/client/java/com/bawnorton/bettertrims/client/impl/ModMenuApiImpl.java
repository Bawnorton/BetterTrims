package com.bawnorton.bettertrims.client.impl;

import com.bawnorton.bettertrims.client.config.ClientConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClientConfigManager::getConfigScreen;
    }
}
