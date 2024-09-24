package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//? if >=1.21
import net.minecraft.component.type.AttributeModifierSlot;

public abstract class TrimEffect {
    private final TagKey<Item> materials;
    private final List<TrimAttribute> entityAttributes;

    public TrimEffect(TagKey<Item> materials) {
        this.materials = materials;
        entityAttributes = new ArrayList<>();
        addAttributes(entityAttributes::add);

        TrimRegistries.TRIM_EFFECTS.createEntry(this);
    }

    protected abstract void addAttributes(Consumer<TrimAttribute> adder);

    protected abstract boolean isEnabled();

    public TagKey<Item> getMaterials() {
        return materials;
    }

    //? if >=1.21 {
    public Set<Identifier> getEffectIds(AttributeModifierSlot slot) {
        return entityAttributes.stream()
                .map(attribute -> attribute.getSlotId(slot))
                .collect(Collectors.toSet());
    }
    //?} else {
    /*public Set<UUID> getEffectIds(EquipmentSlot slot) {
        return entityAttributes.stream()
                .map(attribute -> attribute.getSlotId(slot))
                .collect(Collectors.toSet());
    }
    *///?}

    //? if >=1.21 {
    public void forEachAttribute(AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> biConsumer) {
    //?} else {
    /*public void forEachAttribute(EquipmentSlot slot, BiConsumer<EntityAttribute, EntityAttributeModifier> biConsumer) {
    *///?}
        entityAttributes.forEach(attribute -> {
            EntityAttributeModifier modifier = getAttributeModifier(attribute, slot);
            if (modifier != null) {
                biConsumer.accept(attribute.entry(), modifier);
            }
        });
    }

    //? if >=1.21 {
    private @Nullable EntityAttributeModifier getAttributeModifier(TrimAttribute attribute, AttributeModifierSlot slot) {
        if(attribute.slotPredicate().test(slot)) {
            return new EntityAttributeModifier(attribute.getSlotId(slot), attribute.value(), attribute.operation());
        }
        return null;
    }
    //?} else {
    /*private @Nullable EntityAttributeModifier getAttributeModifier(TrimAttribute attribute, EquipmentSlot slot) {
        if(attribute.slotPredicate().test(slot)) {
            String slotId = attribute.getSlotId(slot).toString();
            return new EntityAttributeModifier(UUID.fromString(slotId), slotId, attribute.value(), attribute.operation());
        }
        return null;
    }
    *///?}

    public boolean matchesMaterial(RegistryEntry<ArmorTrimMaterial> material) {
        if(!isEnabled()) return false;

        return material.value().ingredient().isIn(materials);
    }

    public void readNbt(LivingEntity entity, NbtCompound nbt) {
    }

    public NbtCompound writeNbt(LivingEntity entity, NbtCompound nbt) {
        return nbt;
    }

    public Identifier getId() {
        return TrimRegistries.TRIM_EFFECTS.getId(this);
    }

    public interface Factory<T extends TrimEffect> {
        T create(TagKey<Item> materials);
    }
}
