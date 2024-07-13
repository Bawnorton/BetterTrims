package com.bawnorton.bettertrims.client.platform;

import com.bawnorton.bettertrims.client.BetterTrimsClient;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;

public final class BetterTrimsClientWrapper implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterTrimsClient.init();
    }
}
//?} elif neoforge {
/*import com.bawnorton.bettertrims.BetterTrims;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = BetterTrims.MOD_ID, dist = Dist.CLIENT)
public final class BetterTrimsClientWrapper {
    public BetterTrimsClientWrapper() {
        BetterTrimsClient.init();
    }
}
*///?}
