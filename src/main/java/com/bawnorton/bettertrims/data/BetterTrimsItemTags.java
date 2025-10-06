package com.bawnorton.bettertrims.data;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface BetterTrimsItemTags {
	TagKey<Item> TRIM_PATTERN_TEMPLATES = bind("trim_pattern_templates");

	private static TagKey<Item> bind(String name) {
		return TagKey.create(Registries.ITEM, BetterTrims.rl(name));
	}
}
