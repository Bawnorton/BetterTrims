package com.bawnorton.bettertrims.ability.type;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;

public class WearingGoldTrimAbility implements TrimAbility {
    public static final MapCodec<WearingGoldTrimAbility> CODEC = MapCodec.unit(new WearingGoldTrimAbility());

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.WEARING_GOLD;
    }

    @Override
    public void onAdded(LivingEntity livingEntity, int newCount) {
    }

    @Override
    public void onRemoved(LivingEntity livingEntity, int priorCount) {
    }
}
