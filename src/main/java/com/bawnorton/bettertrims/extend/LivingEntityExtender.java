package com.bawnorton.bettertrims.extend;

import com.bawnorton.bettertrims.util.EquippedStack;

public interface LivingEntityExtender {
    Iterable<EquippedStack> betterTrims$getTrimmables();

    boolean betterTrims$shouldSilverApply();
}
