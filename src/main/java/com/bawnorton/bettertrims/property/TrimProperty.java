package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import com.bawnorton.bettertrims.property.ability.runner.TrimToggleAbilityRunner;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.element.ConditionalElementMatcher;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TrimProperty {
    public static final Codec<TrimProperty> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Matcher.CODEC.fieldOf("trim").forGetter(TrimProperty::matcher),
        TrimAbilityComponents.CODEC.optionalFieldOf("abilities", DataComponentMap.EMPTY).forGetter(TrimProperty::abilities),
        TrimItemPropertyComponents.CODEC.optionalFieldOf("item_properties", DataComponentMap.EMPTY).forGetter(TrimProperty::properties)
    ).apply(instance, TrimProperty::new));

    public static final Codec<Holder<TrimProperty>> CODEC = RegistryFixedCodec.create(BetterTrimsRegistries.Keys.TRIM_PROPERTIES);

    private final Matcher matcher;
    private final DataComponentMap abilities;
    private final DataComponentMap properties;

    private final Map<DataComponentType<?>, List<TrimEntityAbilityRunner<?>>> entityAbilityRunners = new HashMap<>();
    private final Map<DataComponentType<?>, List<TrimToggleAbilityRunner<?>>> toggleAbilityRunners = new HashMap<>();
    private final Map<DataComponentType<?>, List<TrimValueAbilityRunner<?>>> valueAbilityRunners = new HashMap<>();
    private final Map<DataComponentType<?>, List<ElementMatcher<?>>> elementMatchers = new HashMap<>();

    public TrimProperty(Matcher matcher, DataComponentMap abilities, DataComponentMap properties) {
        this.matcher = matcher;
        this.abilities = abilities;
        this.properties = properties;
    }

    public <A extends TrimEntityAbility> List<TrimEntityAbilityRunner<?>> getEntityAbilityRunners(DataComponentType<List<ConditionalElement<A>>> type) {
        return entityAbilityRunners.computeIfAbsent(type, k -> {
            List<ConditionalElement<A>> elements = this.abilities.get(type);
            if (elements == null) return List.of();

            List<TrimEntityAbilityRunner<?>> runners = new ArrayList<>();
            for (ConditionalElement<A> element : elements) {
                runners.add(new TrimEntityAbilityRunner<>(element.element(), element::matches, matcher));
            }
            return runners;
        });
    }

    public <A extends TrimValueAbility> List<TrimValueAbilityRunner<?>> getValueAbilityRunners(DataComponentType<List<ConditionalElement<A>>> type) {
        return valueAbilityRunners.computeIfAbsent(type, k -> {
            List<ConditionalElement<A>> elements = this.abilities.get(type);
            if (elements == null) return List.of();

            List<TrimValueAbilityRunner<?>> runners = new ArrayList<>();
            for (ConditionalElement<A> element : elements) {
                runners.add(new TrimValueAbilityRunner<>(element.element(), element::matches, matcher));
            }
            return runners;
        });
    }

    public <A extends TrimToggleAbility> List<TrimToggleAbilityRunner<?>> getToggleAbilityRunners(DataComponentType<List<ConditionalElement<A>>> type) {
        return toggleAbilityRunners.computeIfAbsent(type, k -> {
            List<ConditionalElement<A>> elements = this.abilities.get(type);
            if (elements == null) return List.of();

            List<TrimToggleAbilityRunner<?>> runners = new ArrayList<>();
            for (ConditionalElement<A> element : elements) {
                runners.add(new TrimToggleAbilityRunner<>(element.element(), element::matches, matcher));
            }
            return runners;
        });
    }

    public <A> List<ElementMatcher<?>> getAbilityElements(DataComponentType<List<ConditionalElement<A>>> type) {
        return elementMatchers.computeIfAbsent(type, k -> getElements(type, abilities));
    }

    public <A> List<ElementMatcher<?>> getItemPropertyElements(DataComponentType<List<ConditionalElement<A>>> type) {
        return elementMatchers.computeIfAbsent(type, k -> getElements(type, properties));
    }

    private <A> @NotNull List<ElementMatcher<?>> getElements(DataComponentType<List<ConditionalElement<A>>> type, DataComponentMap elementMap) {
        List<ConditionalElement<A>> elements = elementMap.get(type);
        if (elements == null) return List.of();

        List<ElementMatcher<?>> matchers = new ArrayList<>();
        for (ConditionalElement<A> element : elements) {
            matchers.add(new ConditionalElementMatcher<>(matcher, element));
        }
        return matchers;
    }

    public Matcher matcher() {
        return matcher;
    }

    public DataComponentMap abilities() {
        return abilities;
    }

    public DataComponentMap properties() {
        return properties;
    }

    public static Builder builder(Matcher matcher) {
        return new Builder(matcher);
    }

    public static class Builder {
        private final Map<DataComponentType<?>, List<?>> abilityLists = new HashMap<>();
        private final DataComponentMap.Builder abilityMapBuilder = DataComponentMap.builder();

        private final Map<DataComponentType<?>, List<?>> itemPropertyLists = new HashMap<>();
        private final DataComponentMap.Builder itemPropertiesMapBuilder = DataComponentMap.builder();

        private final Matcher matcher;

        public Builder(Matcher matcher) {
            this.matcher = matcher;
        }

        public <A> Builder ability(DataComponentType<List<ConditionalElement<A>>> type, A ability) {
            getAbilityList(type).add(new ConditionalElement<>(ability, Optional.empty()));
            return this;
        }

        public <A> Builder ability(DataComponentType<List<ConditionalElement<A>>> type, A ability, LootItemCondition.Builder requirements) {
            getAbilityList(type).add(new ConditionalElement<>(ability, Optional.of(requirements.build())));
            return this;
        }

        public <A> Builder specialAbility(DataComponentType<List<A>> type, A ability) {
            getAbilityList(type).add(ability);
            return this;
        }

        public <A> Builder itemProperty(DataComponentType<List<ConditionalElement<A>>> type, A itemProperty) {
            getItemProperties(type).add(new ConditionalElement<>(itemProperty, Optional.empty()));
            return this;
        }

        public <A> Builder itemProperty(DataComponentType<List<ConditionalElement<A>>> type, A itemProperty, LootItemCondition.Builder requirements) {
            getItemProperties(type).add(new ConditionalElement<>(itemProperty, Optional.of(requirements.build())));
            return this;
        }

        @SuppressWarnings("unchecked")
        private <A> List<A> getAbilityList(DataComponentType<List<A>> type) {
            return (List<A>) abilityLists.computeIfAbsent(
                type, k -> {
                    List<A> list = new ArrayList<>();
                    abilityMapBuilder.set(type, list);
                    return list;
                }
            );
        }

        @SuppressWarnings("unchecked")
        private <A> List<A> getItemProperties(DataComponentType<List<A>> type) {
            return (List<A>) itemPropertyLists.computeIfAbsent(
                type, k -> {
                    List<A> list = new ArrayList<>();
                    itemPropertiesMapBuilder.set(type, list);
                    return list;
                }
            );
        }

        public TrimProperty build() {
            return new TrimProperty(matcher, abilityMapBuilder.build(), itemPropertiesMapBuilder.build());
        }
    }
}
