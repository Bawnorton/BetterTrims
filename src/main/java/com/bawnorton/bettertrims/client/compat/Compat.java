package com.bawnorton.bettertrims.client.compat;

import com.bawnorton.bettertrims.client.compat.lambdynlights.LambDynLightsCompat;
import com.bawnorton.bettertrims.client.compat.mythicmetals.MythicMetalsCompat;
import com.bawnorton.bettertrims.client.compat.sodiumdynlights.SodiumDynLightsCompat;
import com.bawnorton.bettertrims.platform.Platform;
import java.util.Optional;

public final class Compat {
    private static LambDynLightsCompat lambDynLightsCompat;
    private static SodiumDynLightsCompat sodiumDynLightsCompat;
    private static MythicMetalsCompat mythicMetalsCompat;

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

    public static Optional<MythicMetalsCompat> getMythicMetalsCompat() {
        if (!Platform.isModLoaded("mythicmetals")) return Optional.empty();
        if (mythicMetalsCompat == null) mythicMetalsCompat = new MythicMetalsCompat();

        return Optional.of(mythicMetalsCompat);
    }
}