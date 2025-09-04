package com.bawnorton.bettertrims.property.ability;

import com.bawnorton.bettertrims.property.Matcher;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import java.util.function.Function;

public record TrimAbilityRunner(TrimAbility ability, Matcher matcher) {
    public static final Codec<TrimAbilityRunner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TrimAbility.CODEC.fieldOf("ability").forGetter(TrimAbilityRunner::ability),
            Matcher.CODEC.fieldOf("applies_to").forGetter(TrimAbilityRunner::matcher)
    ).apply(instance, TrimAbilityRunner::new));

    public void start(LivingEntity wearer) {
        runAbility(wearer, count -> ability.start(wearer, count));
    }

    public void stop(LivingEntity wearer, ItemStack leftStack, EquipmentSlot forSlot) {
        int count = matcher.getMatchCount(wearer);
        boolean wasContributing = matcher.matches(wearer, leftStack, forSlot);
        int contributingCount = wasContributing ? count + 1 : count;
        if (contributingCount <= 0) return;

        ability.stop(wearer, contributingCount);
        if(count >= matcher.minCount()) {
            ability.start(wearer, count);
        }
    }

    public void tick(LivingEntity wearer) {
        runAbility(wearer, count -> ability.tick(wearer, count));
    }

    public boolean canTarget(LivingEntity wearer, LivingEntity attacker) {
        return mapAbility(wearer, count -> ability.canTarget(wearer, attacker, count), true);
    }

    public boolean isInvulnerableTo(LivingEntity wearer, DamageSource damageSource) {
        return mapAbility(wearer, count -> ability.isInvulnerableTo(wearer, damageSource, count), false);
    }

    public int modifyGainedExperience(LivingEntity wearer, int amount) {
        return mapAbility(wearer, count -> ability.modifyGainedExperience(wearer, amount, count), amount);
    }

    public void onProjectileSpanwed(LivingEntity wearer, Projectile projectile) {
        runAbility(wearer, count -> ability.onProjectileSpawned(wearer, projectile, count));
    }

    public void onProjectileTick(LivingEntity wearer, Projectile projectile) {
        runAbility(wearer, count -> ability.onProjectileTick(wearer, projectile, count));
    }

    public float modifyProjectileDamage(LivingEntity wearer, LivingEntity target, Projectile projectile, float damage) {
        return mapAbility(wearer, count -> ability.modifyProjectileDamage(wearer, target, projectile, damage, count), damage);
    }

    private void runAbility(LivingEntity wearer, Consumer<Integer> abilityRunner) {
        int count = matcher.getMatchCount(wearer);
        if(count >= matcher.minCount()) {
            abilityRunner.accept(count);
        }
    }

    private <T> T mapAbility(LivingEntity wearer, Function<Integer, T> abilityMapper, T fallback) {
        int count = matcher.getMatchCount(wearer);
        if(count >= matcher.minCount()) {
            return abilityMapper.apply(count);
        }
        return fallback;
    }
}
