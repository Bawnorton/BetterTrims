package com.bawnorton.bettertrims.advancement.criterion;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import java.util.Optional;

public final class MagneticHelmetWornCriterion extends AttributeCriterion<MagneticHelmetWornCriterion.Conditions> {
    @Override
    protected ConditionFactory<Conditions> factory() {
        return Conditions::new;
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static AdvancementCriterion<Conditions> create() {
            return TrimCriteria.MAGNETIC_HELMET_WORN.create(new Conditions(Optional.empty()));
        }
    }
}