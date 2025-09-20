package com.bawnorton.bettertrims.networking;

import com.bawnorton.bettertrims.mixin.accessor.SmithingTrimRecipeAccessor;
import com.bawnorton.bettertrims.networking.packet.TrimPatternSourcePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Networking {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(TrimPatternSourcePayload.TYPE, TrimPatternSourcePayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            RecipeManager recipeManager = server.getRecipeManager();
            Map<Holder<TrimPattern>, HolderSet<Item>> patternProviders = new HashMap<>();
            for(RecipeHolder<?> holder : recipeManager.getRecipes()) {
                if(holder.value() instanceof SmithingTrimRecipe recipe) {
                    List<Holder<Item>> patternItems = new ArrayList<>();
                    recipe.templateIngredient().ifPresent(ingredient -> ingredient.items().forEach(patternItems::add));
                    Holder<TrimPattern> patternHolder = ((SmithingTrimRecipeAccessor) recipe).bettertrims$pattern();
                    patternProviders.put(patternHolder, HolderSet.direct(patternItems));
                }
            }
            send(handler.player, new TrimPatternSourcePayload(patternProviders));
        });
    }

    public static <T extends CustomPacketPayload> void send(ServerPlayer player, T payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
