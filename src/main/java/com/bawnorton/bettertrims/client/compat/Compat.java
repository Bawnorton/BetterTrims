package com.bawnorton.bettertrims.client.compat;

import com.bawnorton.bettertrims.client.compat.lambdynlights.LambDynLightsCompat;
import com.bawnorton.bettertrims.platform.Platform;
import java.util.Optional;

public final class Compat {
    public static LambDynLightsCompat dynamicLightsCompat;

    public static Optional<LambDynLightsCompat> getDynamicLightsCompat() {
        if (!Platform.isModLoaded("lambdynlights")) return Optional.empty();
        if (dynamicLightsCompat == null) dynamicLightsCompat = new LambDynLightsCompat();

        return Optional.of(dynamicLightsCompat);
    }
}