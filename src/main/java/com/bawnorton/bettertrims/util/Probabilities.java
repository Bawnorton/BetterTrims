package com.bawnorton.bettertrims.util;

import java.util.Random;

public class Probabilities {
    private final Random rand;

    public Probabilities(long seed) {
        rand = new Random(seed);
    }

    public boolean passes(float percentageChance) {
        return rand.nextFloat() < percentageChance;
    }

    public <T extends Enum<?>> T pickRandom(Class<T> enumClass) {
        return enumClass.getEnumConstants()[rand.nextInt(enumClass.getEnumConstants().length)];
    }

    public boolean passes(double chance) {
        return rand.nextDouble() < chance;
    }
}
