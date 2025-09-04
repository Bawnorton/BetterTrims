package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.property.CountBasedValue;
import com.bawnorton.bettertrims.property.ability.TrimAbility;
import com.bawnorton.bettertrims.property.ability.TrimAbilityType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public record ElectrifyingTrimAbility(HolderSet<EntityType<?>> projectiles, CountBasedValue damage) implements TrimAbility {
    public static final MapCodec<ElectrifyingTrimAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HolderSetCodec.create(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE.holderByNameCodec(), false)
                    .fieldOf("projectile_types")
                    .forGetter(ElectrifyingTrimAbility::projectiles),
            CountBasedValue.CODEC.fieldOf("damage").forGetter(ElectrifyingTrimAbility::damage)
    ).apply(instance, ElectrifyingTrimAbility::new));

    public static ElectrifyingTrimAbility create(HolderSet<EntityType<?>> projectiles, CountBasedValue damage) {
        return new ElectrifyingTrimAbility(projectiles, damage);
    }

    @Override
    public TrimAbilityType<? extends TrimAbility> getType() {
        return TrimAbilityType.ELECTRIFYING;
    }

    @Override
    public float modifyProjectileDamage(LivingEntity wearer, LivingEntity target, Projectile projectile, float damage, int count) {
        if (!projectiles.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(projectile.getType()))) {
            return TrimAbility.super.modifyProjectileDamage(wearer, target, projectile, damage, count);
        }

        if(projectile instanceof ThrownTrident trident && wearer.level() instanceof ServerLevel level && level.isThundering()) {
            ItemEnchantments enchantments = trident.getWeaponItem().get(DataComponents.ENCHANTMENTS);
            boolean hasChanneling = false;
            if(enchantments != null) {
                hasChanneling = level.registryAccess()
                        .get(Enchantments.CHANNELING)
                        .map(enchantments::getLevel)
                        .map(i -> i > 0)
                        .orElse(false);
            }
            if(!hasChanneling) {
                level.registryAccess()
                        .getOrThrow(Enchantments.CHANNELING)
                        .value()
                        .doPostAttack(
                                level,
                                1,
                                new EnchantedItemInUse(trident.getWeaponItem(), EquipmentSlot.MAINHAND, wearer),
                                EnchantmentTarget.VICTIM,
                                target,
                                level.damageSources().trident(trident, wearer)
                        );
            }
        }

        float additionalDamage = this.damage.calculate(count);
        return damage + additionalDamage;
    }

    @Override
    public void onProjectileTick(LivingEntity wearer, Projectile projectile, int count) {
        if (!projectiles.contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(projectile.getType()))) return;

        Level level = wearer.level();
        if(level.isClientSide()) {
            RandomSource random = level.getRandom();
            for(int i = 0; i < 3; i++) {
                level.addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        projectile.getX(),
                        projectile.getY(),
                        projectile.getZ(),
                        random.nextFloat() - 0.5,
                        random.nextFloat() - 0.5,
                        random.nextFloat() - 0.5
                );
            }
        }
    }
}
