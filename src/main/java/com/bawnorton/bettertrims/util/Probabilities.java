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
}
