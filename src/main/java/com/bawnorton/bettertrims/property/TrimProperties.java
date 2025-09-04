package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.ability.type.AttributeTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.DamageResistantAbility;
import com.bawnorton.bettertrims.property.ability.type.EffectTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.ElectrifyingTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.ExperienceGainTrimAbility;
import com.bawnorton.bettertrims.property.ability.type.WearingGoldTrimAbility;
import com.bawnorton.bettertrims.property.item.type.DamageResistantItemProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TrimProperties {
    public static final ResourceKey<TrimProperty> WEARING_GOLD = key("wearing_gold");
    public static final ResourceKey<TrimProperty> INCREASE_MINING_SPEED = key("increase_mining_speed");
    public static final ResourceKey<TrimProperty> SLIGHTLY_TOUGHER = key("slightly_tougher");
    public static final ResourceKey<TrimProperty> MODERATELY_TOUGHER = key("moderately_tougher");
    public static final ResourceKey<TrimProperty> FIREPROOF = key("fireproof");
    public static final ResourceKey<TrimProperty> INCREASE_EXPERIENCE_GAIN = key("increase_experience_gain");
    public static final ResourceKey<TrimProperty> INCREASE_MOVEMENT_SPEED = key("increase_movement_speed");
    public static final ResourceKey<TrimProperty> INCREASE_SWIM_SPEED = key("increase_swim_speed");
    public static final ResourceKey<TrimProperty> CONDUCTIVE = key("conductive");

    public static void bootstrap(BootstrapContext<TrimProperty> context) {
        HolderGetter<TrimMaterial> materialGetter = context.lookup(Registries.TRIM_MATERIAL);
        HolderGetter<DamageType> damageTypeGetter = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<EntityType<?>> entityTypeGetter = context.lookup(Registries.ENTITY_TYPE);
        register(
                context,
                WEARING_GOLD,
                TrimProperty.builder()
                        .ability(
                                new WearingGoldTrimAbility(),
                                getMaterialMatcher(materialGetter, TrimMaterials.GOLD)
                        )
                        .build()
        );
        register(
                context,
                INCREASE_MINING_SPEED,
                TrimProperty.builder()
                        .ability(
                                AttributeTrimAbility.single(
                                        BetterTrims.rl("trim_mining_speed"),
                                        Attributes.MINING_EFFICIENCY,
                                        CountBasedValue.countSquared(1),
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.IRON)
                        )
                        .ability(
                                EffectTrimAbility.single(
                                        MobEffects.HASTE,
                                        CountBasedValue.constant(1)
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.IRON, 4)
                        )
                        .build()
        );
        register(
                context,
                SLIGHTLY_TOUGHER,
                TrimProperty.builder()
                        .ability(
                                AttributeTrimAbility.single(
                                        BetterTrims.rl("trim_armour"),
                                        Attributes.ARMOR,
                                        CountBasedValue.linear(2, 1),
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.DIAMOND)
                        )
                        .build()
        );
        register(
                context,
                MODERATELY_TOUGHER,
                TrimProperty.builder()
                        .ability(
                                AttributeTrimAbility.multiple(
                                        AttributeTrimAbility.modifier(
                                                BetterTrims.rl("trim_armour_toughness"),
                                                Attributes.ARMOR_TOUGHNESS,
                                                CountBasedValue.linear(1),
                                                AttributeModifier.Operation.ADD_VALUE
                                        ),
                                        AttributeTrimAbility.modifier(
                                                BetterTrims.rl("trim_armour"),
                                                Attributes.ARMOR,
                                                CountBasedValue.linear(2),
                                                AttributeModifier.Operation.ADD_VALUE
                                        )
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.NETHERITE)
                        )
                        .build()
        );
        register(
                context,
                FIREPROOF,
                TrimProperty.builder()
                        .ability(
                                EffectTrimAbility.single(
                                        MobEffects.FIRE_RESISTANCE,
                                        CountBasedValue.constant(0)
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.NETHERITE)
                        )
                        .itemProperty(
                                DamageResistantItemProperty.create(damageTypeGetter.getOrThrow(DamageTypeTags.IS_FIRE)),
                                getMaterialMatcher(materialGetter, TrimMaterials.NETHERITE)
                        )
                        .build()
        );
        register(
                context,
                INCREASE_EXPERIENCE_GAIN,
                TrimProperty.builder()
                        .ability(
                                new ExperienceGainTrimAbility(CountBasedValue.linear(1, 0.05f)),
                                getMaterialMatcher(materialGetter, TrimMaterials.QUARTZ)
                        )
                        .build()
        );
        register(
                context,
                INCREASE_MOVEMENT_SPEED,
                TrimProperty.builder()
                        .ability(
                                AttributeTrimAbility.single(
                                        BetterTrims.rl("trim_movement_speed"),
                                        Attributes.MOVEMENT_SPEED,
                                        CountBasedValue.linear(0.1f),
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.REDSTONE)
                        )
                        .build()
        );
        register(
                context,
                CONDUCTIVE,
                TrimProperty.builder()
                        .itemProperty(
                                DamageResistantItemProperty.create(damageTypeGetter.getOrThrow(DamageTypeTags.IS_LIGHTNING)),
                                getMaterialMatcher(materialGetter, TrimMaterials.COPPER)
                        )
                        .ability(
                                DamageResistantAbility.create(damageTypeGetter.getOrThrow(DamageTypeTags.IS_LIGHTNING)),
                                getMaterialMatcher(materialGetter, TrimMaterials.COPPER)
                        )
                        .ability(
                                ElectrifyingTrimAbility.create(
                                        entityTypeGetter.getOrThrow(EntityTypeTags.IMPACT_PROJECTILES),
                                        CountBasedValue.constant(6)
                                ),
                                getMaterialMatcher(materialGetter, TrimMaterials.COPPER, 4)
                        )
                        .build()
        );
    }

    private static @NotNull Matcher getMaterialMatcher(HolderGetter<TrimMaterial> materialGetter, ResourceKey<TrimMaterial> material) {
        return getMaterialMatcher(materialGetter, material, 1);
    }

    private static @NotNull Matcher getMaterialMatcher(HolderGetter<TrimMaterial> materialGetter, ResourceKey<TrimMaterial> material, int count) {
        return Matcher.forMaterial(HolderSet.direct(materialGetter.getOrThrow(material)), count);
    }

    public static Registry<TrimProperty> getProperties(Level level) {
        return level.registryAccess().lookupOrThrow(BetterTrimsRegistries.TRIM_PROPERTIES);
    }

    private static void register(BootstrapContext<TrimProperty> context, ResourceKey<TrimProperty> key, TrimProperty property) {
        context.register(key, property);
    }

    private static ResourceKey<TrimProperty> key(String key) {
        return ResourceKey.create(BetterTrimsRegistries.TRIM_PROPERTIES, BetterTrims.rl(key));
    }
}
