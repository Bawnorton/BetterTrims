package com.bawnorton.bettertrims.property.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public record DimensionCheck(HolderSet<DimensionType> dimensions) implements LootItemCondition {
    public static final MapCodec<DimensionCheck> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.DIMENSION_TYPE).fieldOf("dimensions").forGetter(DimensionCheck::dimensions)
    ).apply(instance, DimensionCheck::new));

    public static LootItemConditionType TYPE;

    public static DimensionCheck.Builder of(HolderSet<DimensionType> dimensions) {
        return new Builder(dimensions);
    }

    @Override
    public @NotNull LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        return dimensions().contains(context.getLevel().dimensionTypeRegistration());
    }

    public record Builder(HolderSet<DimensionType> dimensions) implements LootItemCondition.Builder {
        @Override
        public @NotNull LootItemCondition build() {
            return new DimensionCheck(dimensions);
        }
    }
}
