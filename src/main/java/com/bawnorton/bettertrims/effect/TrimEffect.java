package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class TrimEffect {
    {
        TrimRegistries.TRIM_EFFECTS.createEntry(this);
    }

    private final TagKey<Item> materials;
    private final List<TrimAttribute> entityAttributes;

    protected TrimEffect(TagKey<Item> materials) {
        this.materials = materials;
        entityAttributes = new ArrayList<>();
        addAttributes(entityAttributes::add);
    }

    protected abstract void addAttributes(Consumer<TrimAttribute> adder);

    public TagKey<Item> getMaterials() {
        return materials;
    }

    public Set<Identifier> getEffectIds(AttributeModifierSlot slot) {
        return entityAttributes.stream()
                .map(attribute -> attribute.getSlotId(slot))
                .collect(Collectors.toSet());
    }

    public void forEachAttribute(AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> biConsumer) {
        entityAttributes.forEach(attribute -> biConsumer.accept(attribute.entry(), getAttributeModifier(attribute, slot)));
    }

    private @NotNull EntityAttributeModifier getAttributeModifier(TrimAttribute attribute, AttributeModifierSlot slot) {
        return new EntityAttributeModifier(attribute.getSlotId(slot), attribute.value(), attribute.operation());
    }

    public boolean matchesMaterial(RegistryEntry<ArmorTrimMaterial> material) {
        return material.value().ingredient().isIn(materials);
    }

    public void readNbt(LivingEntity entity, NbtCompound nbt) {
    }

    public NbtCompound writeNbt(LivingEntity entity, NbtCompound nbt) {
        return nbt;
    }

    public interface Factory<T extends TrimEffect> {
        T create(TagKey<Item> materials);
    }
}
