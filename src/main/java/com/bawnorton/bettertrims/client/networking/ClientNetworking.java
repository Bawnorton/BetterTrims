package com.bawnorton.bettertrims.client.networking;

//? if fabric {

import com.bawnorton.bettertrims.networking.packet.TrimPatternSourcePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(TrimPatternSourcePayload.TYPE, TrimPatternSourcePayload::handle);
	}
}
//?} else {
/*public class ClientNetworking {
	public static void init() {
	}
}
*///?}
