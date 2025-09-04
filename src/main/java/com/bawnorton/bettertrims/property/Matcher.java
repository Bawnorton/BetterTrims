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
    ).apply(instance, Matcher::create));

    public static final Codec<Matcher> COUNTLESS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HolderSetCodec.create(Registries.TRIM_MATERIAL, TrimMaterial.CODEC, false)
                    .optionalFieldOf("material", HolderSet.empty())
                    .forGetter(Matcher::material),
            HolderSetCodec.create(Registries.TRIM_PATTERN, TrimPattern.CODEC, false)
                    .optionalFieldOf("pattern", HolderSet.empty())
                    .forGetter(Matcher::pattern)
    ).apply(instance, (material, pattern) -> Matcher.create(material, pattern, 1)));

    private static Matcher create(HolderSet<TrimMaterial> material, HolderSet<TrimPattern> pattern, int minCount) {
        if(material.size() == 0 && pattern.size() == 0) {
            throw new UnsupportedOperationException("'applies_to' must specify at least one material or pattern");
        }
        return new Matcher(material, pattern, minCount);
    }

    public static Matcher forMaterial(HolderSet<TrimMaterial> material, int minCount) {
        return new Matcher(material, HolderSet.empty(), minCount);
    }

    public static Matcher forPattern(HolderSet<TrimPattern> pattern, int minCount) {
        return new Matcher(HolderSet.empty(), pattern, minCount);
    }

    public Integer getMatchCount(LivingEntity wearer) {
        int matchCount = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = wearer.getItemBySlot(slot);
            EquipmentSlot shouldBeInSlot = wearer.getEquipmentSlotForItem(stack);
            if (slot != shouldBeInSlot) continue;

            if (matches(stack)) {
                matchCount++;
            }
        }
        return matchCount;
    }

    public boolean matches(LivingEntity willBeWearer, ItemStack willBeWorn, EquipmentSlot forSlot) {
        EquipmentSlot shouldBeInSlot = willBeWearer.getEquipmentSlotForItem(willBeWorn);
        return shouldBeInSlot == forSlot && matches(willBeWorn);
    }

    public boolean matches(ItemStack stack) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim == null) return false;

        Holder<TrimMaterial> material = trim.material();
        Holder<TrimPattern> pattern = trim.pattern();
        boolean matchesMaterial = this.material.size() == 0 || this.material.contains(material);
        boolean matchesPattern = this.pattern.size() == 0 || this.pattern.contains(pattern);
        return matchesMaterial && matchesPattern;
    }
}
