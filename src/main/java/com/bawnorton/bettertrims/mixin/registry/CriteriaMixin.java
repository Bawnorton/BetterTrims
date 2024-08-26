package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.advancement.criterion.WalkingFurnaceSmeltedCriteron;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Criteria.class)
public abstract class CriteriaMixin {
    static {
        TrimCriteria.WALKING_FURNACE_SMELTED = bettertrims$register("walking_furnace_smelted", new WalkingFurnaceSmeltedCriteron());
    }

    @Unique
    private static <T extends Criterion<?>> T bettertrims$register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, BetterTrims.id(id), criterion);
    }
}
