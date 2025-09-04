package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.CountBasedValue;
import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.List;

public record AttributeTrimAbility(List<TrimAttributeModifier> modifiers) implements TrimAbility {
    public static final MapCodec<AttributeTrimAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TrimAttributeModifier.CODEC.listOf().fieldOf("modifiers").forGetter(AttributeTrimAbility::modifiers)
    ).apply(instance, AttributeTrimAbility::new));

    public static AttributeTrimAbility multiple(TrimAttributeModifier... modifiers) {
        return new AttributeTrimAbility(List.of(modifiers));
    }

    public static AttributeTrimAbility single(ResourceLocation id, Holder<Attribute> attribute, CountBasedValue value, AttributeModifier.Operation operation) {
        return multiple(modifier(id, attribute, value, operation));
    }

    public static AttributeTrimAbility.TrimAttributeModifier modifier(ResourceLocation id, Holder<Attribute> attribute, CountBasedValue value, AttributeModifier.Operation operation) {
        return TrimAttributeModifier.create(id, attribute, value, operation);
    }

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.ATTRIBUTE;
    }

    @Override
    public  void start(LivingEntity livingEntity, int newCount) {
        livingEntity.getAttributes().addTransientAttributeModifiers(makeAttributeMap(newCount));
    }

    @Override
    public void stop(LivingEntity livingEntity, int priorCount) {
        livingEntity.getAttributes().removeAttributeModifiers(makeAttributeMap(priorCount));
    }

    private Multimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int count) {
        Multimap<Holder<Attribute>, AttributeModifier> multimap = HashMultimap.create();
        for(TrimAttributeModifier modifier : modifiers) {
            multimap.put(modifier.attribute(), modifier.getAttributeModifier(count));
        }
        return multimap;
    }

    public record TrimAttributeModifier(ResourceLocation id, Holder<Attribute> attribute, CountBasedValue value, AttributeModifier.Operation operation) {
        private static final Codec<TrimAttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(TrimAttributeModifier::id),
                Attribute.CODEC.fieldOf("attribute").forGetter(TrimAttributeModifier::attribute),
                CountBasedValue.CODEC.fieldOf("value").forGetter(TrimAttributeModifier::value),
                AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(TrimAttributeModifier::operation)
        ).apply(instance, TrimAttributeModifier::new));

        public static TrimAttributeModifier create(ResourceLocation id, Holder<Attribute> attribute, CountBasedValue value, AttributeModifier.Operation operation) {
            return new TrimAttributeModifier(id, attribute, value, operation);
        }

        private AttributeModifier getAttributeModifier(int count) {
            return new AttributeModifier(id, value.calculate(count), operation);
        }
    }
}
