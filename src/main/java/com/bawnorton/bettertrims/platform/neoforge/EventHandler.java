//? if neoforge {
/*package com.bawnorton.bettertrims.platform.neoforge;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.function.UnaryOperator;

@EventBusSubscriber
public final class EventHandler {
	@SubscribeEvent
	public static void addResourcePacks(AddPackFindersEvent event) {
		event.addPackFinders(
				BetterTrims.rl("data/bettertrims/datapacks/default"),
				PackType.SERVER_DATA,
				Component.translatable("bettertrims.resourcepack.default"),
				PackSource.create(UnaryOperator.identity(), true),
				false,
				Pack.Position.TOP
		);

		event.addPackFinders(
				BetterTrims.rl("data/bettertrims/datapacks/trim_effects"),
				PackType.SERVER_DATA,
				Component.translatable("bettertrims.resourcepack.effects"),
				PackSource.create(UnaryOperator.identity(), false),
				false,
				Pack.Position.TOP
		);
	}
}
*///?}