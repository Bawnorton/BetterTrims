package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.Registry;

public final class CountBasedValueTypes {
    public static final CountBasedValueType<CountBasedValue.Clamped> CLAMPED = register("clamped", new CountBasedValueType<>(CountBasedValue.Clamped.CODEC));
    public static final CountBasedValueType<CountBasedValue.Constant> CONSTANT = register("constant", new CountBasedValueType<>(CountBasedValue.Constant.CODEC));
    public static final CountBasedValueType<CountBasedValue.Fraction> FRACTION = register("fraction", new CountBasedValueType<>(CountBasedValue.Fraction.CODEC));
    public static final CountBasedValueType<CountBasedValue.CountSquared> COUNT_SQUARED = register("count_squared", new CountBasedValueType<>(CountBasedValue.CountSquared.CODEC));
    public static final CountBasedValueType<CountBasedValue.Linear> LINEAR = register("linear", new CountBasedValueType<>(CountBasedValue.Linear.CODEC));
    public static final CountBasedValueType<CountBasedValue.Lookup> LOOKUP = register("lookup", new CountBasedValueType<>(CountBasedValue.Lookup.CODEC));

    public static void init() {
        //no-op
    }

    private static <T extends CountBasedValue> CountBasedValueType<T> register(String path, CountBasedValueType<T> type) {
        return Registry.register(CountBasedValueType.REGISTRY, BetterTrims.rl(path), type);
    }
}
