package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class TrimEffect<T> {
    private final TagKey<Item> materials;
    private final List<RegistryEntry<EntityAttribute>> entityAttributes;

    protected TrimEffect(TagKey<Item> materials) {
        this.materials = materials;
        entityAttributes = new ArrayList<>();
        addAttributes(entityAttributes::add);
    }

    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
    }

    public TrimEffectApplicator<T> getApplicator() {
        return TrimEffectApplicator.none();
    }

    public Set<Identifier> getEffectIds(AttributeModifierSlot slot) {
        return entityAttributes.stream()
                .map(RegistryEntry::getIdAsString)
                .map(id -> getSlotId(id, slot.asString()))
                .collect(Collectors.toSet());
    }

    public void forEachAttribute(AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> biConsumer) {
        entityAttributes.forEach(attributeEntry -> biConsumer.accept(attributeEntry, getAttributeModifier(attributeEntry, slot)));
    }

    protected @NotNull EntityAttributeModifier getAttributeModifier(RegistryEntry<EntityAttribute> entry, AttributeModifierSlot slot) {
        return new EntityAttributeModifier(getSlotId(entry.getIdAsString(), slot.asString()), 1, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    @NotNull
    protected Identifier getSlotId(String id, String slot) {
        return Identifier.of("%s_trimmed_%s".formatted(id, slot));
    }

    public boolean matchesMaterial(RegistryEntry<ArmorTrimMaterial> material) {
        return material.value().ingredient().isIn(materials);
    }

    public boolean matches(Object obj) {
        if(!(obj instanceof LivingEntity livingEntity)) return false;

        AttributeContainer container = livingEntity.getAttributes();
        for(RegistryEntry<EntityAttribute> entry : entityAttributes) {
            if (!container.hasAttribute(entry)) return false;

            double value = container.getValue(entry);
            double defaultValue = entry.value().getDefaultValue();
            if(MathHelper.approximatelyEquals(value, defaultValue)) return false;
        }
        return true;
    }

    public interface Factory<T extends TrimEffect<?>> {
        T create(TagKey<Item> materials);
    }
}
