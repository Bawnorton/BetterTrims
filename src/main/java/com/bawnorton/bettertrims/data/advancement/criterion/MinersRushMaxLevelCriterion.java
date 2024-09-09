package com.bawnorton.bettertrims.data.advancement.criterion;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import java.util.Optional;

public final class MinersRushMaxLevelCriterion extends AttributeCriterion<MinersRushMaxLevelCriterion.Conditions> {
    @Override
    protected ConditionFactory<Conditions> factory() {
        return Conditions::new;
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static AdvancementCriterion<Conditions> create() {
            return TrimCriteria.MINERS_RUSH_MAX_LEVEL.create(new Conditions(Optional.empty()));
        }
    }
}