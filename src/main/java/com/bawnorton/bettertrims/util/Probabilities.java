package com.bawnorton.bettertrims.util;

import java.util.Random;

public class Probabilities {
    private final Random rand = new Random();

    public boolean passes(float percentageChance) {
        return rand.nextFloat() < percentageChance;
    }

    public boolean passes(double percentageChance) {
        return rand.nextDouble() < percentageChance;
    }
}
