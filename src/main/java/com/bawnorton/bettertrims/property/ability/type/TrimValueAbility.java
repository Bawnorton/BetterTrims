package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.AllOf;
import com.bawnorton.bettertrims.property.ability.type.value.AddValue;
import com.bawnorton.bettertrims.property.ability.type.value.MultiplyValue;
import com.bawnorton.bettertrims.property.ability.type.value.RemoveBinomial;
import com.bawnorton.bettertrims.property.ability.type.value.SetValue;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import java.util.function.Function;

public interface TrimValueAbility {
	Codec<TrimValueAbility> CODEC = BetterTrimsRegistries.TRIM_VALUE_ABILITY_TYPE
		.byNameCodec()
		.dispatch(TrimValueAbility::codec, Function.identity());

	static MapCodec<? extends TrimValueAbility> bootstrap(Registry<MapCodec<? extends TrimValueAbility>> registry) {
		Registry.register(registry, BetterTrims.rl("add"), AddValue.CODEC);
		Registry.register(registry, BetterTrims.rl("all_of"), AllOf.ValueAbilities.CODEC);
		Registry.register(registry, BetterTrims.rl("multiply"), MultiplyValue.CODEC);
		Registry.register(registry, BetterTrims.rl("remove_binomial"), RemoveBinomial.CODEC);
		return Registry.register(registry, BetterTrims.rl("set"), SetValue.CODEC);
	}

    static AddValue add(CountBasedValue value) {
        return new AddValue(value);
    }

    static MultiplyValue multiply(CountBasedValue value) {
        return new MultiplyValue(value);
    }

    static RemoveBinomial removeBinomial(CountBasedValue chance) {
        return new RemoveBinomial(chance);
    }

    static SetValue set(CountBasedValue value) {
        return new SetValue(value);
    }

	float process(int count, RandomSource random, float value);

	MapCodec<? extends TrimValueAbility> codec();

}