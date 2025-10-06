package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.condition.DimensionCheck;
import com.mojang.serialization.MapCodec;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment
@Mixin(LootItemConditions.class)
abstract class LootItemConditionsMixin {
	static {
		DimensionCheck.TYPE = bettertrims$register("dimension_check", DimensionCheck.CODEC);
	}

	@Unique
	private static LootItemConditionType bettertrims$register(String name, MapCodec<? extends LootItemCondition> codec) {
		return Registry.register(
				BuiltInRegistries.LOOT_CONDITION_TYPE,
				BetterTrims.rl(name),
				new LootItemConditionType(codec)
		);
	}
}
