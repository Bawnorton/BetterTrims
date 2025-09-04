package com.bawnorton.bettertrims.property.ability;

import com.mojang.serialization.Codec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

public interface TrimAbility {
    Codec<TrimAbility> CODEC = TrimAbilityType.REGISTRY.byNameCodec().dispatch(TrimAbility::getType, TrimAbilityType::codec);

    TrimAbilityType<? extends TrimAbility> getType();

    default void start(LivingEntity livingEntity, int newCount) {
    }

    default void stop(LivingEntity livingEntity, int priorCount) {
    }

    default void tick(LivingEntity livingEntity, int count) {
    }

    default boolean canTarget(LivingEntity wearer, LivingEntity attacker, int count) {
        return true;
    }

    default int modifyGainedExperience(LivingEntity wearer, int amount, int count) {
        return amount;
    }

    default boolean isInvulnerableTo(LivingEntity wearer, DamageSource source, int count) {
        return false;
    }

    default void onProjectileSpawned(LivingEntity wearer, Projectile projectile, int count) {
    }

    default float modifyProjectileDamage(LivingEntity wearer, LivingEntity target, Projectile projectile, float damage, int count) {
        return damage;
    }

    default void onProjectileTick(LivingEntity wearer, Projectile projectile, int count) {
    }
}
