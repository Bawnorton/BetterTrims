package com.bawnorton.bettertrims.property.item.type;

import com.bawnorton.bettertrims.property.item.TrimItemProperty;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;

public record DamageResistantItemProperty(HolderSet<DamageType> damageType) implements TrimItemProperty {
    public static final MapCodec<DamageResistantItemProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HolderSetCodec.create(Registries.DAMAGE_TYPE, DamageType.CODEC, false)
                    .fieldOf("damage_type")
                    .forGetter(DamageResistantItemProperty::damageType)
    ).apply(instance, DamageResistantItemProperty::new));

    public static DamageResistantItemProperty create(HolderSet<DamageType> type) {
        return new DamageResistantItemProperty(type);
    }

    @Override
    public TrimItemPropertyType<? extends TrimItemProperty> getType() {
        return TrimItemPropertyType.DAMAGE_RESISTANT;
    }

    @Override
    public boolean isInvulnerableTo(ItemStack stack, DamageSource source) {
        return damageType.contains(source.typeHolder());
    }
}
