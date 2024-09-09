package com.bawnorton.bettertrims.data.advancement.criterion;

import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import java.util.Optional;
import java.util.function.Function;

public abstract class AttributeCriterion<C extends AbstractCriterion.Conditions> extends AbstractCriterion<C> {
    private final Function<AttributeCriterion<C>, Codec<C>> CONDITION_CODEC_GETTER = Util.memoize(criterion -> RecordCodecBuilder.create(
            instance -> instance.group(
                    EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player)
            ).apply(instance, criterion.factory()::create)
    ));

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, Predicates.alwaysTrue());
    }

    @Override
    public Codec<C> getConditionsCodec() {
        return CONDITION_CODEC_GETTER.apply(this);
    }

    protected abstract ConditionFactory<C> factory();

    public interface ConditionFactory<C> {
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        C create(Optional<LootContextPredicate> player);
    }
}
