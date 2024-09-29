package com.bawnorton.bettertrims.data.advancement.criterion;


import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;
import java.util.Optional;

//? if <1.21
/*import net.minecraft.advancement.criterion.AbstractCriterionConditions;*/

public final class EchoingTriggeredCriterion extends AttributeCriterion<EchoingTriggeredCriterion.Conditions> {
    @Override
    protected ConditionFactory<Conditions> factory() {
        return Conditions::new;
    }

    //? if >=1.21 {
    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static AdvancementCriterion<Conditions> create() {
            return TrimCriteria.ECHOING_TRIGGERED.create(new Conditions(Optional.empty()));
        }
    }
    //?} else {
    /*public static final class Conditions extends AbstractCriterionConditions {
        public Conditions() {
            super(ID, LootContextPredicate.EMPTY);
        }

        public static Conditions create() {
            return new Conditions();
        }
    }

    public static final Identifier ID = BetterTrims.id("echoing_triggered");

    @Override
    public Identifier getId() {
        return ID;
    }
    *///?}
}
