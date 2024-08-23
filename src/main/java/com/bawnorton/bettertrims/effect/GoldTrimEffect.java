package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;

public final class GoldTrimEffect extends CelestialTrimEffect {
    public GoldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected RegistryEntry<EntityAttribute> getCelestialAttribute() {
        return TrimEntityAttributes.SUNS_BLESSING;
    }

    @Override
    protected boolean canApply(World world) {
        return world.isDay();
    }
}
