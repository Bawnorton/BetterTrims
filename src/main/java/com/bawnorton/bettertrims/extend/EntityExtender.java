package com.bawnorton.bettertrims.extend;

import net.minecraft.item.ItemStack;

public interface EntityExtender {
    Iterable<ItemStack> betterTrims$getTrimmables();

    boolean betterTrims$shouldSilverApply();
}
