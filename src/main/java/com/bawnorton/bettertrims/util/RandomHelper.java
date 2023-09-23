package com.bawnorton.bettertrims.util;

import net.minecraft.util.math.random.Random;

public abstract class RandomHelper {
    private static final Random RANDOM = Random.create(System.currentTimeMillis());

    public static float nextFloat() {
        return RANDOM.nextFloat();
    }
}
