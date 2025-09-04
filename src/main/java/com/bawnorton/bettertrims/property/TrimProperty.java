package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityRunner;
import com.bawnorton.bettertrims.property.item.TrimItemProperty;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyRunner;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import java.util.ArrayList;
import java.util.List;

public record TrimProperty(List<TrimAbilityRunner> abilityHolders, List<TrimItemPropertyRunner> propertyHolders) {
    public static final Codec<TrimProperty> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TrimAbilityRunner.CODEC.listOf().optionalFieldOf("abilities", List.of()).forGetter(TrimProperty::abilityHolders),
            TrimItemPropertyRunner.CODEC.listOf().optionalFieldOf("item_properties", List.of()).forGetter(TrimProperty::propertyHolders)
    ).apply(instance, TrimProperty::create));

    private static TrimProperty create(List<TrimAbilityRunner> abilityHolders, List<TrimItemPropertyRunner> propertyHolders) {
        if(abilityHolders.isEmpty() && propertyHolders.isEmpty()) {
            throw new UnsupportedOperationException("Trim properties must specify at least one ability or item property");
        }
        return new TrimProperty(abilityHolders, propertyHolders);
    }

    public static final Codec<Holder<TrimProperty>> CODEC = RegistryFixedCodec.create(BetterTrimsRegistries.TRIM_PROPERTIES);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<TrimAbilityRunner> abilities = new ArrayList<>();
        private final List<TrimItemPropertyRunner> itemProperties = new ArrayList<>();

        public Builder ability(TrimAbility ability, Matcher counter) {
            abilities.add(new TrimAbilityRunner(ability, counter));
            return this;
        }

        public Builder itemProperty(TrimItemProperty itemProperty, Matcher counter) {
            itemProperties.add(new TrimItemPropertyRunner(itemProperty, counter));
            return this;
        }

        public TrimProperty build() {
            return TrimProperty.create(abilities, itemProperties);
        }
    }
}
