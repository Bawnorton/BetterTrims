package com.bawnorton.bettertrims.data;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.DimensionType;

public interface BetterTrimsDimensionTypeTags {
	TagKey<DimensionType> HAS_SUN = bind("has_sun");
	TagKey<DimensionType> HAS_MOON = bind("has_moon");

	private static TagKey<DimensionType> bind(String name) {
		return TagKey.create(Registries.DIMENSION_TYPE, BetterTrims.rl(name));
	}
}
