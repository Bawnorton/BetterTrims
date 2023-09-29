package com.bawnorton.bettertrims.util;

import java.util.Random;

public abstract class RandomHelper {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static float nextFloat() {
        return RANDOM.nextFloat();
    }
}
