package com.bawnorton.util;

import java.util.Random;

public abstract class RandomHelper {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static float nextFloat() {
        flush();
        return RANDOM.nextFloat();
    }

    private static void flush() {
        for (int i = 0; i < 10; i++) {
            RANDOM.nextFloat();
        }
    }
}
