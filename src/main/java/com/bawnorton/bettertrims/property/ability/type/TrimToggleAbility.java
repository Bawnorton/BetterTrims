package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.AllOf;
import com.bawnorton.bettertrims.property.ability.type.toggle.AttributeAbility;
import com.bawnorton.bettertrims.property.ability.type.toggle.ToggleMobEffectAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.element.TrimElement;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public interface TrimToggleAbility extends TrimElement {
	Codec<TrimToggleAbility> CODEC = BetterTrimsRegistries.TRIM_TOGGLE_ABILITY_TYPE
			.byNameCodec()
			.dispatch(TrimToggleAbility::codec, Function.identity());

	static MapCodec<? extends TrimToggleAbility> bootstrap(Registry<MapCodec<? extends TrimToggleAbility>> registry) {
		Registry.register(registry, BetterTrims.rl("all_of"), AllOf.ToggleAbilities.CODEC);
		Registry.register(registry, BetterTrims.rl("attribute"), AttributeAbility.CODEC);
		return Registry.register(registry, BetterTrims.rl("toggle_mob_effect"), ToggleMobEffectAbility.CODEC);
	}

	void start(ServerLevel level, LivingEntity wearer, TrimmedItems items);

	default void stop(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
	}

	MapCodec<? extends TrimToggleAbility> codec();
}
