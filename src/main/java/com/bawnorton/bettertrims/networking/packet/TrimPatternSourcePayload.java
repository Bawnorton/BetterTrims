package com.bawnorton.bettertrims.networking.packet;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.BetterTrimsClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.HashMap;
import java.util.Map;

public record TrimPatternSourcePayload(
		Map<Holder<TrimPattern>, HolderSet<Item>> patternSources) implements CustomPacketPayload {
	public static final Type<TrimPatternSourcePayload> TYPE = new Type<>(BetterTrims.rl("trim_pattern_source"));
	public static final StreamCodec<RegistryFriendlyByteBuf, TrimPatternSourcePayload> CODEC = ByteBufCodecs.map(
			HashMap::new,
			ByteBufCodecs.holderRegistry(Registries.TRIM_PATTERN),
			ByteBufCodecs.holderSet(Registries.ITEM)
	).map(TrimPatternSourcePayload::new, payload -> new HashMap<>(payload.patternSources()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(ClientPlayNetworking.Context context) {
		BetterTrimsClient.setPatternSources(patternSources);
	}
}
