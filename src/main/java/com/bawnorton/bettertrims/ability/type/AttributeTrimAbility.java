package com.bawnorton.bettertrims.ability.type;

import com.bawnorton.bettertrims.ability.CountBasedValue;
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

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.ATTRIBUTE;
    }

    @Override
    public  void onAdded(LivingEntity livingEntity, int newCount) {
        livingEntity.getAttributes().addTransientAttributeModifiers(makeAttributeMap(newCount));
    }

    @Override
    public void onRemoved(LivingEntity livingEntity, int priorCount) {
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

        private AttributeModifier getAttributeModifier(int count) {
            return new AttributeModifier(id, value.calculate(count), operation);
        }
    }
}
