package com.bawnorton.bettertrims.property.ability;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.context.TrimContextParamSets;
import com.bawnorton.bettertrims.property.ability.type.misc.DamageImmunityAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.ability.type.misc.PiglinSafeAbility;
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

public interface TrimAbilityComponents {
	Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE::byNameCodec);

	Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

	Map<ResourceLocation, Component> TOOLTIPS = new HashMap<>();

	DataComponentType<List<ConditionalElement<TrimValueAbility>>> INCOMING_DAMAGE = register(
			"incoming_damage",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_DAMAGE).listOf())
	);
	DataComponentType<List<ConditionalElement<DamageImmunityAbility>>> DAMAGE_IMMUNITY = register(
			"damage_immunity",
			builder -> builder.persistent(ConditionalElement.ability(DamageImmunityAbility.CODEC, TrimContextParamSets.TRIM_DAMAGE).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimValueAbility>>> DAMAGE = register(
			"damage",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_DAMAGE).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimValueAbility>>> ARMOUR_EFFECTIVENESS = register(
			"armour_effectiveness",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_DAMAGE).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimEntityAbility>>> POST_ATTACK = register(
			"post_attack",
			builder -> builder.persistent(ConditionalElement.ability(TrimEntityAbility.CODEC, TrimContextParamSets.TRIM_DAMAGE).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimEntityAbility>>> HIT_BLOCK = register(
			"hit_block",
			builder -> builder.persistent(ConditionalElement.ability(TrimEntityAbility.CODEC, TrimContextParamSets.HIT_BLOCK_WITH_HELD_ITEM).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimValueAbility>>> ITEM_DAMAGE = register(
			"item_damage",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_EQUIPMENT).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimToggleAbility>>> EQUIPPED = register(
			"equipped",
			builder -> builder.persistent(ConditionalElement.ability(TrimToggleAbility.CODEC, TrimContextParamSets.TRIM_EQUIPMENT).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimEntityAbility>>> TICK = register(
			"tick",
			builder -> builder.persistent(ConditionalElement.ability(TrimEntityAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimEntityAbility>>> SECOND = register(
			"second",
			builder -> builder.persistent(ConditionalElement.ability(TrimEntityAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimEntityAbility>>> PROJECTILE_TICK = register(
			"projectile_tick",
			builder -> builder.persistent(ConditionalElement.ability(TrimEntityAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimValueAbility>>> EXPERIENCE_GAINED = register(
			"experience_gained",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);
	DataComponentType<List<ConditionalElement<TrimValueAbility>>> TRADE_COST = register(
			"trade_cost",
			builder -> builder.persistent(ConditionalElement.ability(TrimValueAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);
	DataComponentType<List<ConditionalElement<PiglinSafeAbility>>> PIGLIN_SAFE = register(
			"piglin_safe",
			builder -> builder.persistent(ConditionalElement.ability(PiglinSafeAbility.CODEC, TrimContextParamSets.TRIM_ENTITY).listOf())
	);

	private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
		ResourceLocation id = BetterTrims.rl(name);
		TOOLTIPS.put(id, Component.translatable("bettertrims.tooltip.component." + name));
		return Registry.register(
				BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE,
				id,
				operator.apply(DataComponentType.builder()).build()
		);
	}

	static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> ignored) {
		return PIGLIN_SAFE;
	}
}
