package com.bawnorton.bettertrims.platform;

import com.bawnorton.bettertrims.BetterTrims;

//? if fabric {
/*import net.fabricmc.api.ModInitializer;

public final class BetterTrimsWrapper implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterTrims.init();
    }
}
*///?} elif neoforge {
import net.neoforged.fml.common.Mod;

@Mod(BetterTrims.MOD_ID)
public final class BetterTrimsWrapper {
    public BetterTrimsWrapper() {
        BetterTrims.init();
    }
}
//?}
