package com.bawnorton.bettertrims.property.count;

import com.bawnorton.bettertrims.client.tooltip.util.Formatter;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.google.common.base.Predicates;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface CountBasedValue {
	Codec<CountBasedValue> DISPATCH_CODEC = CountBasedValueType.REGISTRY.byNameCodec()
			.dispatch(CountBasedValue::getType, CountBasedValueType::codec);

	Codec<CountBasedValue> CODEC = Codec.either(CountBasedValue.Constant.LITERAL_CODEC, DISPATCH_CODEC)
			.xmap(
					either -> either.map(Function.identity(), Function.identity()),
					levelBasedValue -> levelBasedValue instanceof Constant constant ? Either.left(constant) : Either.right(levelBasedValue)
			);

	static Clamped clamped(CountBasedValue value, float min, float max) {
		return new Clamped(value, min, max);
	}

	static Constant constant(float value) {
		return new Constant(value);
	}

	static Fraction fraction(CountBasedValue numerator, CountBasedValue denominator) {
		return new Fraction(numerator, denominator);
	}

	static CountSquared countSquared(float constant) {
		return new CountSquared(constant);
	}

	static Linear linear(float base, float perCountAboveFirst) {
		return new Linear(base, perCountAboveFirst);
	}

	static Linear linear(float value) {
		return new Linear(value, value);
	}

	static Lookup lookup(List<Float> values, CountBasedValue fallback) {
		return new Lookup(values, fallback);
	}

	float calculate(int count);

	default List<Float> getValues(int upToCount) {
		return getValues(upToCount, Predicates.alwaysTrue());
	}

	default List<Float> getValues(int upToCount, Predicate<Float> filter) {
		List<Float> values = new ArrayList<>();
		for (int i = 1; i <= upToCount; i++) {
			float value = calculate(i);
			if (!filter.test(value)) continue;

			values.add(value);
		}
		return values;
	}

	default List<Component> getValueComponents(int upToCount, boolean includeCount) {
		return getValueComponents(upToCount, includeCount, Formatter::decimal);
	}

	default List<Component> getValueComponents(int upToCount, boolean includeCount, Function<Float, Component> formatter) {
		return getValueComponents(upToCount, includeCount, formatter, Predicates.alwaysTrue());
	}

	default List<Component> getValueComponents(int upToCount, boolean includeCount, Function<Float, Component> formatter, Predicate<Float> filter) {
		List<Float> values = getValues(upToCount).stream().map(f -> filter.test(f) ? f : null).toList();
		if (values.stream().distinct().count() == 1) {
			return List.of(Styler.number(formatter.apply(values.getFirst()).copy()));
		}
		return getValueComponentsInternal(values, includeCount, formatter);
	}

	private List<Component> getValueComponentsInternal(List<Float> values, boolean includeCount, Function<Float, Component> formatter) {
		List<Component> components = new ArrayList<>();
		for (int i = 1; i <= values.size(); i++) {
			Float value = values.get(i - 1);
			if(value == null) continue;

			Component valueComponent = Styler.number(formatter.apply(value).copy());
			MutableComponent countComponent = Styler.trim(Component.literal("[%d]".formatted(i))).append(": ");
			if (includeCount) {
				components.add(countComponent.append(valueComponent));
			} else {
				components.add(valueComponent);
			}
		}
		return components;
	}

	CountBasedValueType<? extends CountBasedValue> getType();

	record Clamped(CountBasedValue value, float min, float max) implements CountBasedValue {
		public static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				CountBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value),
				Codec.FLOAT.fieldOf("min").forGetter(Clamped::min),
				Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)
		).apply(instance, Clamped::new));

		@Override
		public float calculate(int count) {
			return Mth.clamp(value.calculate(count), min, max);
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.CLAMPED;
		}
	}

	record Constant(float value) implements CountBasedValue {
		public static final Codec<Constant> LITERAL_CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
		public static final MapCodec<Constant> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
		).apply(instance, Constant::new));

		@Override
		public float calculate(int count) {
			return value;
		}

		@Override
		public List<Component> getValueComponents(int upToCount, boolean includeCount, Function<Float, Component> formatter) {
			return List.of(Styler.number(formatter.apply(value).copy()));
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.CONSTANT;
		}
	}

	record Fraction(CountBasedValue numerator, CountBasedValue denominator) implements CountBasedValue {
		public static final MapCodec<Fraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				CountBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator),
				CountBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)
		).apply(instance, Fraction::new));

		@Override
		public float calculate(int count) {
			float denominator = this.denominator.calculate(count);
			return denominator == 0.0F ? 0.0F : this.numerator.calculate(count) / denominator;
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.FRACTION;
		}
	}

	record CountSquared(float constant) implements CountBasedValue {
		public static final MapCodec<CountSquared> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.fieldOf("constant").forGetter(CountSquared::constant)
		).apply(instance, CountSquared::new));

		@Override
		public float calculate(int count) {
			return Mth.square(count) + constant;
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.COUNT_SQUARED;
		}
	}

	record Linear(float base, float perCountAboveFirst) implements CountBasedValue {
		public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.fieldOf("base").forGetter(Linear::base),
				Codec.FLOAT.fieldOf("per_count_above_first").forGetter(Linear::perCountAboveFirst)
		).apply(instance, Linear::new));

		@Override
		public float calculate(int count) {
			return base + perCountAboveFirst * (count - 1);
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.LINEAR;
		}
	}

	record Lookup(List<Float> values, CountBasedValue fallback) implements CountBasedValue {
		public static final MapCodec<Lookup> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values),
				CountBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)
		).apply(instance, Lookup::new));

		@Override
		public float calculate(int count) {
			return count <= this.values.size() ? this.values.get(count - 1) : this.fallback.calculate(count);
		}

		@Override
		public CountBasedValueType<? extends CountBasedValue> getType() {
			return CountBasedValueTypes.LOOKUP;
		}
	}
}
