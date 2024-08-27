package com.bawnorton.bettertrims.effect;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public abstract class CelestialEffect extends TrimEffect {
    protected CelestialEffect(TagKey<Item> materials) {
        super(materials);
    }
}
