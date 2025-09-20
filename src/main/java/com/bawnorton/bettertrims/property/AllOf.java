package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.item.type.TrimItemProperty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Function;

public interface AllOf {
    static <T, A extends T> MapCodec<A> codec(Codec<T> codec, Function<A, List<T>> getter, Function<List<T>, A> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec.listOf().fieldOf("abilities").forGetter(getter)
        ).apply(instance, factory));
    }

    static ToggleAbilities toggleAbilities(TrimToggleAbility... abilities) {
        return new ToggleAbilities(List.of(abilities));
    }

    static EntityAbilities entityAbilities(TrimEntityAbility... abilities) {
        return new EntityAbilities(List.of(abilities));
    }

    static ValueAbilities valueAbilities(TrimValueAbility... abilities) {
        return new ValueAbilities(List.of(abilities));
    }

    static ItemProperties itemProperties(TrimItemProperty... properties) {
        return new ItemProperties(List.of(properties));
    }

    record ToggleAbilities(List<TrimToggleAbility> abilities) implements TrimToggleAbility {
        public static final MapCodec<ToggleAbilities> CODEC = AllOf.codec(TrimToggleAbility.CODEC, ToggleAbilities::abilities, ToggleAbilities::new);

        @Override
        public void start(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
            for (TrimToggleAbility ability : abilities) {
                ability.start(level, wearer, items);
            }
        }

        @Override
        public void stop(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
            for (TrimToggleAbility ability : abilities) {
                ability.stop(level, wearer, items);
            }
        }

        @Override
        public MapCodec<? extends TrimToggleAbility> codec() {
            return CODEC;
        }
    }

    record EntityAbilities(List<TrimEntityAbility> abilities) implements TrimEntityAbility {
        public static final MapCodec<EntityAbilities> CODEC = AllOf.codec(TrimEntityAbility.CODEC, EntityAbilities::abilities, EntityAbilities::new);

        @Override
        public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
            for (TrimEntityAbility ability : abilities) {
                ability.apply(level, wearer, target, items, targetSlot, origin);
            }
        }

        @Override
        public MapCodec<? extends TrimEntityAbility> codec() {
            return CODEC;
        }
    }

    record ValueAbilities(List<TrimValueAbility> values) implements TrimValueAbility {
        public static final MapCodec<ValueAbilities> CODEC = AllOf.codec(TrimValueAbility.CODEC, ValueAbilities::values, ValueAbilities::new);

        @Override
        public float process(int count, RandomSource random, float value) {
            for (TrimValueAbility v : values) {
                value = v.process(count, random, value);
            }
            return value;
        }

        @Override
        public MapCodec<? extends TrimValueAbility> codec() {
            return CODEC;
        }
    }

    record ItemProperties(List<TrimItemProperty> properties) implements TrimItemProperty {
        public static final MapCodec<ItemProperties> CODEC = AllOf.codec(TrimItemProperty.CODEC, ItemProperties::properties, ItemProperties::new);

        @Override
        public MapCodec<? extends TrimItemProperty> codec() {
            return CODEC;
        }
    }
}
