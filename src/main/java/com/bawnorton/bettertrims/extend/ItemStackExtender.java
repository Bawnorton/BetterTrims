package com.bawnorton.bettertrims.extend;

import net.minecraft.entity.LivingEntity;

public interface ItemStackExtender {
    LivingEntity bettertrims$getWearer();
    void bettertrims$setWearer(LivingEntity wearer);
}
