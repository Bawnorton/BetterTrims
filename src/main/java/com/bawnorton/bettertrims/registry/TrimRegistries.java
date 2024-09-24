package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.effect.TrimEffect;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.MutableRegistry;

public final class TrimRegistries {
    //? if >=1.21 {
    public static MutableRegistry<TrimEffect> TRIM_EFFECTS;
    //?} else {
    /*public static DefaultedRegistry<TrimEffect> TRIM_EFFECTS;
    *///?}
}
