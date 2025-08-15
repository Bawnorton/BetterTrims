package com.bawnorton.bettertrims.ability;

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
import java.util.function.Function;

public record ApplicationPredicate(HolderSet<TrimMaterial> material, HolderSet<TrimPattern> pattern, int minCount) implements Function<LivingEntity, Integer> {
    public static final Codec<ApplicationPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HolderSetCodec.create(Registries.TRIM_MATERIAL, TrimMaterial.CODEC, false)
                    .lenientOptionalFieldOf("material", HolderSet.empty())
                    .forGetter(ApplicationPredicate::material),
            HolderSetCodec.create(Registries.TRIM_PATTERN, TrimPattern.CODEC, false)
                    .lenientOptionalFieldOf("pattern", HolderSet.empty())
                    .forGetter(ApplicationPredicate::pattern),
            ExtraCodecs.intRange(1, 255)
                    .fieldOf("min_count")
                    .forGetter(ApplicationPredicate::minCount)
    ).apply(instance, ApplicationPredicate::create));

    private static ApplicationPredicate create(HolderSet<TrimMaterial> material, HolderSet<TrimPattern> pattern, int minCount) {
        if(material.size() == 0 && pattern.size() == 0) {
            throw new UnsupportedOperationException("Applies to must specify at least one material or pattern");
        }
        return new ApplicationPredicate(material, pattern, minCount);
    }

    @Override
    public Integer apply(LivingEntity livingEntity) {
        int matchCount = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = livingEntity.getItemBySlot(slot);
            EquipmentSlot shouldBeInSlot = livingEntity.getEquipmentSlotForItem(stack);
            if (slot != shouldBeInSlot) continue;

            ArmorTrim trim = stack.get(DataComponents.TRIM);
            if (trim == null) continue;

            Holder<TrimMaterial> wornMaterial = trim.material();
            Holder<TrimPattern> wornPattern = trim.pattern();
            boolean matchesMaterial = material.size() == 0 || material.contains(wornMaterial);
            boolean matchesPattern = pattern.size() == 0 || pattern.contains(wornPattern);
            if (!(matchesMaterial && matchesPattern)) continue;

            matchCount++;
        }
        return matchCount;
    }
}
