package com.bawnorton.bettertrims.data.advancement.criterion;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import java.util.Optional;

public final class WalkingFurnaceSmeltedCriteron extends AttributeCriterion<WalkingFurnaceSmeltedCriteron.Conditions> {
    @Override
    protected ConditionFactory<Conditions> factory() {
        return Conditions::new;
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static AdvancementCriterion<WalkingFurnaceSmeltedCriteron.Conditions> create() {
            return TrimCriteria.WALKING_FURNACE_SMELTED.create(new Conditions(Optional.empty()));
        }
    }
}
