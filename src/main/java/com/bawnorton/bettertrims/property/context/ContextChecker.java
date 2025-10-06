package com.bawnorton.bettertrims.property.context;

import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Predicate;

public interface ContextChecker {
	Predicate<LootContext> conditionChecker();

	default boolean checkRequirement(LootContext lootContext) {
		return conditionChecker().test(lootContext);
	}
}
