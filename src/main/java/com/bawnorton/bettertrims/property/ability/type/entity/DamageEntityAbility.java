package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record DamageEntityAbility(CountBasedValue minDamage, CountBasedValue maxDamage, Holder<DamageType> damageType) implements TrimEntityAbility {
    public static final MapCodec<DamageEntityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntityAbility::minDamage),
        CountBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntityAbility::maxDamage),
        DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntityAbility::damageType)
    ).apply(instance, DamageEntityAbility::new));

    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        int count = items.size();
        float damage = Mth.randomBetween(target.getRandom(), minDamage.calculate(count), maxDamage.calculate(count));
        //? if 1.21.8 {
        target.hurtServer(level, new DamageSource(damageType, wearer), damage);
        //?} elif 1.21.1 {
        /*target.hurt(new DamageSource(damageType, wearer), damage);
        *///?}
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return CODEC;
    }
}
