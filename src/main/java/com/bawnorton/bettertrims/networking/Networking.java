package com.bawnorton.bettertrims.networking;

import com.bawnorton.bettertrims.mixin.accessor.SmithingTrimRecipeAccessor;
import com.bawnorton.bettertrims.networking.packet.TrimPatternSourcePayload;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.*;

//? if fabric {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//?} else {
/*import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
*///?}
public class Networking {
	private static final Map<Holder<TrimPattern>, HolderSet<Item>> patternProviders = new HashMap<>();

	//? if fabric {
	public static void init() {
		PayloadTypeRegistry.playS2C().register(TrimPatternSourcePayload.TYPE, TrimPatternSourcePayload.CODEC);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
				send(handler.player, new TrimPatternSourcePayload(patternProviders))
		);

		ServerLifecycleEvents.SERVER_STARTED.register(Networking::updatePatternProviders);
	}

	public static <T extends CustomPacketPayload> void send(ServerPlayer player, T payload) {
		ServerPlayNetworking.send(player, payload);
	}
	//?} else {
	/*public static void init() {
	}

	@SubscribeEvent
	public static void registerPackets(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(TrimPatternSourcePayload.TYPE, TrimPatternSourcePayload.CODEC, TrimPatternSourcePayload::handle);
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;

		send(player, new TrimPatternSourcePayload(patternProviders));
	}

	@SubscribeEvent
	public static void onServerStart(ServerStartedEvent event) {
		updatePatternProviders(event.getServer());
	}

	public static <T extends CustomPacketPayload> void send(ServerPlayer player, T payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}
	*///?}

	private static void updatePatternProviders(MinecraftServer server) {
		RecipeManager recipeManager = server.getRecipeManager();
		patternProviders.clear();
		for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
			if (holder.value() instanceof SmithingTrimRecipe recipe) {
				List<Holder<Item>> patternItems = new ArrayList<>();
				//? if >=1.21.8 {
				recipe.templateIngredient().ifPresent(ingredient -> ingredient.items().forEach(patternItems::add));
				Holder<TrimPattern> patternHolder = ((SmithingTrimRecipeAccessor) recipe).bettertrims$pattern();
				//?} else {
				/*SmithingTrimRecipeAccessor accessor = (SmithingTrimRecipeAccessor) recipe;
				Ingredient templateIngredient = accessor.bettertrims$template();
				List<ItemStack> templateItems = Arrays.asList(templateIngredient.getItems());
				List<Holder.Reference<TrimPattern>> patternHolders = templateItems.stream().map(stack -> TrimPatterns.getFromTemplate(server.registryAccess(), stack))
						.flatMap(Optional::stream)
						.toList();
				Holder<TrimPattern> patternHolder = patternHolders.getFirst();
				if (patternHolder == null) continue;

				templateItems.forEach(stack -> {
					Item item = stack.getItem();
					patternItems.add(Holder.direct(item));
				});
				*///?}
				patternProviders.put(patternHolder, HolderSet.direct(patternItems));
			}
		}
	}
}