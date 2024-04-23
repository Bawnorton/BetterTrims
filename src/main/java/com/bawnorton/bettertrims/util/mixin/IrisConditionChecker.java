package com.bawnorton.bettertrims.util.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import java.util.Optional;

public class IrisConditionChecker implements AdvancedConditionChecker {
    private final Version minVersion;
    private final Version maxVersion;

    public IrisConditionChecker(String min, String max) {
        try {
            minVersion = min.isEmpty() ? null : Version.parse(min);
            maxVersion = max.isEmpty() ? null : Version.parse(max);
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldApply() {
        return versionCheck();
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean versionCheck() {
        Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer("iris");
        if (optional.isEmpty()) return false;

        if (minVersion == null && maxVersion == null) return true;

        Version version = optional.get().getMetadata().getVersion();
        if (minVersion != null && version.compareTo(minVersion) < 0) return false;
        if (maxVersion != null && version.compareTo(maxVersion) > 0) return false;

        return true;
    }
}
