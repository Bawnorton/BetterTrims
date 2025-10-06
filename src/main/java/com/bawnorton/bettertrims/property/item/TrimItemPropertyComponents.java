package com.bawnorton.bettertrims.property.item;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.context.TrimContextParamSets;
import com.bawnorton.bettertrims.property.item.type.DamageImmunityItemProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public interface TrimItemPropertyComponents {
	Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE::byNameCodec);

	Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

	Map<ResourceLocation, Component> TOOLTIPS = new HashMap<>();

	DataComponentType<List<ConditionalElement<DamageImmunityItemProperty>>> DAMAGE_IMMUNITY = register(
			"damage_immunity",
			builder -> builder.persistent(ConditionalElement.itemProperty(DamageImmunityItemProperty.CODEC, TrimContextParamSets.TRIM_ITEM_DAMAGE).listOf())
	);

	private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
		ResourceLocation id = BetterTrims.rl(name);
		TOOLTIPS.put(id, Component.translatable("bettertrims.tooltip.component.%s".formatted(name)));
		return Registry.register(
				BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE,
				id,
				operator.apply(DataComponentType.builder()).build()
		);
	}

	static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) {
		return DAMAGE_IMMUNITY;
	}
}
