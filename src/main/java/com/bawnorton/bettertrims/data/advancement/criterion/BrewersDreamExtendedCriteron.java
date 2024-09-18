package com.bawnorton.bettertrims.data.advancement.criterion;


import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;
import java.util.Optional;

//? if <1.21
import net.minecraft.advancement.criterion.AbstractCriterionConditions;

public final class BrewersDreamExtendedCriteron extends AttributeCriterion<BrewersDreamExtendedCriteron.Conditions> {
    @Override
    protected ConditionFactory<Conditions> factory() {
        return Conditions::new;
    }

    //? if >=1.21 {
    /*public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static AdvancementCriterion<Conditions> create() {
            return TrimCriteria.BREWERS_DREAM_EXTENDED.create(new Conditions(Optional.empty()));
        }
    }
    *///?} else {
    public static final class Conditions extends AbstractCriterionConditions {
        public static LootContextPredicate player;

        public Conditions(Optional<LootContextPredicate> entity) {
            super(ID, entity.orElse(null));
        }
    }

    public static final Identifier ID = BetterTrims.id("brewers_dream_extended");

    @Override
    public Identifier getId() {
        return ID;
    }
    //?}
}
