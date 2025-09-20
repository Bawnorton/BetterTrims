package com.bawnorton.bettertrims.property.item;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.context.TrimContextParamSets;
import com.bawnorton.bettertrims.property.item.type.DamageImmunityItemProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import java.util.List;
import java.util.function.UnaryOperator;

public interface TrimItemPropertyComponents {
    Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE::byNameCodec);

    Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

    DataComponentType<List<ConditionalElement<DamageImmunityItemProperty>>> DAMAGE_IMMUNITY = register(
        "damage_immunity",
        builder -> builder.persistent(ConditionalElement.itemProperty(DamageImmunityItemProperty.CODEC, TrimContextParamSets.TRIM_ITEM_DAMAGE).listOf())
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return Registry.register(
            BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE,
            BetterTrims.rl(name),
            operator.apply(DataComponentType.builder()).build()
        );
    }

    static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) {
        return DAMAGE_IMMUNITY;
    }
}
