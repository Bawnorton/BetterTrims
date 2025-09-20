package com.bawnorton.bettertrims.property.context;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.util.context.ContextKey;

public final class TrimContextParams {
    public static final ContextKey<TrimmedItems> ITEMS = new ContextKey<>(BetterTrims.rl("items"));
}
