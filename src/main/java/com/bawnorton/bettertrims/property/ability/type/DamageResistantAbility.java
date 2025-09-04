package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public record DamageResistantAbility(HolderSet<DamageType> damageType) implements TrimAbility {
    public static final MapCodec<DamageResistantAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HolderSetCodec.create(Registries.DAMAGE_TYPE, DamageType.CODEC, false)
                    .fieldOf("damage_type")
                    .forGetter(DamageResistantAbility::damageType)
    ).apply(instance, DamageResistantAbility::new));

    public static DamageResistantAbility create(HolderSet<DamageType> damageType) {
        return new DamageResistantAbility(damageType);
    }

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.DAMAGE_RESISTANT;
    }

    @Override
    public boolean isInvulnerableTo(LivingEntity wearer, DamageSource source, int count) {
        return damageType.contains(source.typeHolder());
    }
}
