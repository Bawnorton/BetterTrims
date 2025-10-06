package com.bawnorton.bettertrims.data;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface BetterTrimsEntityTypeTags {
	TagKey<EntityType<?>> CONDUCTIVE_PROJECTILES = bind("conductive_projectiles");

	private static TagKey<EntityType<?>> bind(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, BetterTrims.rl(name));
	}
}
