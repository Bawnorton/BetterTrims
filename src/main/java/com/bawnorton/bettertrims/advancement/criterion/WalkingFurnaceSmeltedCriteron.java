package com.bawnorton.bettertrims.advancement.criterion;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.Optional;

public final class WalkingFurnaceSmeltedCriteron extends AbstractCriterion<WalkingFurnaceSmeltedCriteron.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return WalkingFurnaceSmeltedCriteron.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, Predicates.alwaysTrue());
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<WalkingFurnaceSmeltedCriteron.Conditions> create() {
            return TrimCriteria.WALKING_FURNACE_SMELTED.create(new Conditions(Optional.empty()));
        }
    }
}
