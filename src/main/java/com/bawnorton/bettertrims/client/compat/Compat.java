package com.bawnorton.bettertrims.client.compat;

import com.bawnorton.bettertrims.client.compat.lambdynlights.LambDynLightsCompat;
import com.bawnorton.bettertrims.client.compat.sodiumdynlights.SodiumDynLightsCompat;
import com.bawnorton.bettertrims.platform.Platform;
import java.util.Optional;

public final class Compat {
    public static LambDynLightsCompat lambDynLightsCompat;
    public static SodiumDynLightsCompat sodiumDynLightsCompat;

    public static Optional<LambDynLightsCompat> getLambDynLightsCompat() {
        if (!Platform.isModLoaded("lambdynlights")) return Optional.empty();
        if (lambDynLightsCompat == null) lambDynLightsCompat = new LambDynLightsCompat();

        return Optional.of(lambDynLightsCompat);
    }

    public static Optional<SodiumDynLightsCompat> getSodiumDynLightsCompat() {
        if (!Platform.isModLoaded("sodiumdynamiclights")) return Optional.empty();
        if (sodiumDynLightsCompat == null) sodiumDynLightsCompat = new SodiumDynLightsCompat();

        return Optional.of(sodiumDynLightsCompat);
    }
}