package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class WearingGoldTrimAbility implements TrimAbility {
    public static final MapCodec<WearingGoldTrimAbility> CODEC = MapCodec.unit(new WearingGoldTrimAbility());

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.WEARING_GOLD;
    }

    @Override
    public boolean canTarget(LivingEntity wearer, LivingEntity attacker, int count) {
        if(attacker.getBrain().checkMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryStatus.REGISTERED)) {
            return count < 1;
        }
        return TrimAbility.super.canTarget(wearer, attacker, count);
    }
}
