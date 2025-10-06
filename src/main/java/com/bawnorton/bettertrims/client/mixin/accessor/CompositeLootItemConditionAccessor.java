package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.level.storage.loot.predicates.CompositeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@MixinEnvironment("client")
@Mixin(CompositeLootItemCondition.class)
public interface CompositeLootItemConditionAccessor {
	@Accessor("terms")
	List<LootItemCondition> bettertrims$terms();
}
