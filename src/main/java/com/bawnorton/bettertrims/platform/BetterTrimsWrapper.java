package com.bawnorton.bettertrims.platform;

import com.bawnorton.bettertrims.BetterTrims;

//? if fabric {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint
public final class BetterTrimsWrapper implements ModInitializer {
	@Override
	public void onInitialize() {
		BetterTrims.init();
	}
}
//?} else if neoforge {
/*import net.neoforged.fml.common.Mod;

@Mod(BetterTrims.MOD_ID)
public final class BetterTrimsWrapper {
	public BetterTrimsWrapper() {
		BetterTrims.init();
	}
}
*///?}
