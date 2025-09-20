package com.bawnorton.bettertrims.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import java.util.HashMap;
import java.util.Map;

public record Matcher(HolderSet<TrimMaterial> material, HolderSet<TrimPattern> pattern, int minCount) {
    public static final Codec<Matcher> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HolderSetCodec.create(Registries.TRIM_MATERIAL, TrimMaterial.CODEC, false)
            .optionalFieldOf("material", HolderSet.empty())
            .forGetter(Matcher::material),
        HolderSetCodec.create(Registries.TRIM_PATTERN, TrimPattern.CODEC, false)
            .optionalFieldOf("pattern", HolderSet.empty())
            .forGetter(Matcher::pattern),
        ExtraCodecs.intRange(1, 255)
            .optionalFieldOf("min_count", 1)
            .forGetter(Matcher::minCount)
    ).apply(instance, Matcher::new));

    public static Matcher forMaterial(HolderSet<TrimMaterial> material, int minCount) {
        return new Matcher(material, HolderSet.empty(), minCount);
    }

    public static Matcher forPattern(HolderSet<TrimPattern> pattern, int minCount) {
        return new Matcher(HolderSet.empty(), pattern, minCount);
    }

    public Map<EquipmentSlot, ItemStack> getMatchingStacks(LivingEntity wearer) {
        Map<EquipmentSlot, ItemStack> stacks = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = wearer.getItemBySlot(slot);
            EquipmentSlot shouldBeInSlot = wearer.getEquipmentSlotForItem(stack);
            if (slot != shouldBeInSlot) continue;

            if(matches(stack)) {
                stacks.put(slot, stack);
            }
        }
        return stacks;
    }

    public boolean matches(LivingEntity wearer, ItemStack stack, EquipmentSlot slot) {
        EquipmentSlot shouldBeInSlot = wearer.getEquipmentSlotForItem(stack);
        return shouldBeInSlot == slot && matches(stack);
    }

    public boolean matches(ItemStack stack) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim == null) return false;

        return matches(trim);
    }

    public boolean matches(ArmorTrim trim) {
        Holder<TrimMaterial> material = trim.material();
        Holder<TrimPattern> pattern = trim.pattern();
        boolean matchesMaterial = this.material.contains(material) || (!(this.material instanceof HolderSet.Named) && this.material.size() == 0);
        boolean matchesPattern = this.pattern.contains(pattern) || (!(this.pattern instanceof HolderSet.Named) && this.pattern.size() == 0);
        return matchesMaterial && matchesPattern;
    }
}
