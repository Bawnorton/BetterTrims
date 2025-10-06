package com.bawnorton.bettertrims.property.ability.type.misc;

import com.bawnorton.bettertrims.property.element.TrimElement;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public record PiglinSafeAbility() implements TrimElement {
	public static final PiglinSafeAbility INSTANCE = new PiglinSafeAbility();
	public static final Codec<PiglinSafeAbility> CODEC = Codec.unit(() -> INSTANCE);

	public boolean entityLooksForGold(LivingEntity entity) {
		return entity.getBrain().checkMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryStatus.REGISTERED);
	}
}
