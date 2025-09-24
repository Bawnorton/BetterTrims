package com.bawnorton.bettertrims.property;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.BetterTrimsDimensionTypeTags;
import com.bawnorton.bettertrims.data.BetterTrimsEntityTypeTags;
import com.bawnorton.bettertrims.data.TrimMaterialTags;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.ability.type.entity.PlaySoundAbility;
import com.bawnorton.bettertrims.property.ability.type.entity.SpawnParticlesAbility;
import com.bawnorton.bettertrims.property.ability.type.entity.SummonEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.misc.DamageImmunityAbility;
import com.bawnorton.bettertrims.property.ability.type.misc.PiglinSafeAbility;
import com.bawnorton.bettertrims.property.ability.type.toggle.AttributeAbility;
import com.bawnorton.bettertrims.property.ability.type.toggle.ToggleMobEffectAbility;
import com.bawnorton.bettertrims.property.condition.DimensionCheck;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.bawnorton.bettertrims.property.item.type.DamageImmunityItemProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.component.predicates.TrimPredicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public interface TrimProperties {
    ResourceKey<TrimProperty> DUMMY = key("dummy");
    ResourceKey<TrimProperty> CONDUCTIVE = key("conductive");
    ResourceKey<TrimProperty> FIREPROOF = key("fireproof");
    ResourceKey<TrimProperty> IMPROVED_TRADING = key("improved_trading");
    ResourceKey<TrimProperty> INCREASE_EXPERIENCE_GAIN = key("increase_experience_gain");
    ResourceKey<TrimProperty> INCREASE_MINING_SPEED = key("increase_mining_speed");
    ResourceKey<TrimProperty> INCREASE_MOVEMENT_SPEED = key("increase_movement_speed");
    ResourceKey<TrimProperty> LUNAR_BONUSES = key("lunar_bonuses");
    ResourceKey<TrimProperty> TOUGHER = key("tougher");
    ResourceKey<TrimProperty> RESILIENT = key("resilient");
    ResourceKey<TrimProperty> SOLAR_BONUSES = key("solar_bonuses");
    ResourceKey<TrimProperty> WEARING_GOLD = key("wearing_gold");

    static void bootstrap(BootstrapContext<TrimProperty> context) {
        HolderGetter<TrimMaterial> materialGetter = context.lookup(Registries.TRIM_MATERIAL);
        HolderGetter<EntityType<?>> entityTypeGetter = context.lookup(Registries.ENTITY_TYPE);
        HolderGetter<Enchantment> enchantmentGetter = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<DimensionType> dimensionGetter = context.lookup(Registries.DIMENSION_TYPE);
        register(
            context,
            DUMMY,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.RESIN))
                .ability(
                    TrimAbilityComponents.HIT_BLOCK,
                    new SummonEntityAbility(EntityType.LIGHTNING_BOLT),
                    MatchTool.toolMatches(ItemPredicate.Builder.item()
                        .withComponents(DataComponentMatchers.Builder.components()
                            .partial(
                                DataComponentPredicates.POTIONS, new PotionsPredicate(
                                    HolderSet.direct(Potions.HARMING))
                                ).build()
                        )
                    )
                )
                .build()
        );
        register(
            context,
            WEARING_GOLD,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.GOLD))
                .ability(
                    TrimAbilityComponents.PIGLIN_SAFE,
                    PiglinSafeAbility.INSTANCE
                )
                .build()
        );
        register(
            context,
            INCREASE_MINING_SPEED,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.IRON))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    new AttributeAbility(
                        BetterTrims.rl("trim_mining_speed"),
                        Attributes.MINING_EFFICIENCY,
                        CountBasedValue.countSquared(1),
                        AttributeModifier.Operation.ADD_VALUE
                    )
                )
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    new ToggleMobEffectAbility(
                        MobEffects.HASTE,
                        CountBasedValue.constant(1)
                    ),
                    wearingFullSet(materialGetter, TrimMaterialTags.IRON)
                )
                .build()
        );
        register(
            context,
            RESILIENT,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.DIAMOND))
                .ability(
                    TrimAbilityComponents.INCOMING_DAMAGE,
                    TrimValueAbility.multiply(CountBasedValue.linear(0.95f, -0.05f)),
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_RESISTANCE))
                    )
                )
                .ability(
                    TrimAbilityComponents.ITEM_DAMAGE,
                    TrimValueAbility.removeBinomial(
                        CountBasedValue.fraction(
                            CountBasedValue.linear(1),
                            CountBasedValue.linear(4, 1)
                        )
                    ),
                    MatchTool.toolMatches(itemMaterialPredicate(materialGetter, TrimMaterialTags.DIAMOND))
                )
                .build()
        );
        register(
            context,
            TOUGHER,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.NETHERITE))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    AllOf.toggleAbilities(
                        new AttributeAbility(
                            BetterTrims.rl("trim_armour_toughness"),
                            Attributes.ARMOR_TOUGHNESS,
                            CountBasedValue.linear(1),
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_armour"),
                            Attributes.ARMOR,
                            CountBasedValue.linear(2),
                            AttributeModifier.Operation.ADD_VALUE
                        )
                    )
                )
                .ability(
                    TrimAbilityComponents.ITEM_DAMAGE,
                    TrimValueAbility.removeBinomial(
                        CountBasedValue.fraction(
                            CountBasedValue.linear(1),
                            CountBasedValue.linear(2, 1)
                        )
                    ),
                    MatchTool.toolMatches(itemMaterialPredicate(materialGetter, TrimMaterialTags.NETHERITE))
                )
                .build()
        );
        register(
            context,
            FIREPROOF,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.NETHERITE))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    new ToggleMobEffectAbility(
                        MobEffects.FIRE_RESISTANCE,
                        CountBasedValue.constant(0)
                    )
                )
                .itemProperty(
                    TrimItemPropertyComponents.DAMAGE_IMMUNITY,
                    DamageImmunityItemProperty.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.is(DamageTypeTags.IS_FIRE))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                    )
                )
                .build()
        );
        register(
            context,
            INCREASE_EXPERIENCE_GAIN,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.QUARTZ))
                .ability(
                    TrimAbilityComponents.EXPERIENCE_GAINED,
                    TrimValueAbility.multiply(CountBasedValue.linear(1, 0.25f))
                )
                .build()
        );
        register(
            context,
            INCREASE_MOVEMENT_SPEED,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.REDSTONE))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    new AttributeAbility(
                        BetterTrims.rl("trim_movement_speed"),
                        Attributes.MOVEMENT_SPEED,
                        CountBasedValue.linear(0.12f),
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
                )
                .build()
        );
        register(
            context,
            CONDUCTIVE,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.COPPER))
                .ability(
                    TrimAbilityComponents.DAMAGE_IMMUNITY,
                    DamageImmunityAbility.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.is(DamageTypeTags.IS_LIGHTNING))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                    )
                )
                .ability(
                    TrimAbilityComponents.PROJECTILE_TICK,
                    new SpawnParticlesAbility(
                        ParticleTypes.ELECTRIC_SPARK,
                        SpawnParticlesEffect.offsetFromEntityPosition(0),
                        SpawnParticlesEffect.offsetFromEntityPosition(0),
                        SpawnParticlesEffect.movementScaled(-0.5f),
                        SpawnParticlesEffect.movementScaled(-0.5f),
                        ConstantFloat.of(1)
                    ),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                            .entityType(getEntityTypePredicate(entityTypeGetter, BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES))
                    )
                )
                .ability(
                    TrimAbilityComponents.DAMAGE,
                    TrimValueAbility.add(CountBasedValue.linear(0.5F)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.DIRECT_ATTACKER,
                        EntityPredicate.Builder.entity()
                            .entityType(getEntityTypePredicate(entityTypeGetter, BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES))
                    )
                )
                .ability(
                    TrimAbilityComponents.POST_ATTACK,
                    AllOf.entityAbilities(
                        new SummonEntityAbility(EntityType.LIGHTNING_BOLT),
                        new PlaySoundAbility(SoundEvents.TRIDENT_THUNDER, ConstantFloat.of(5F), ConstantFloat.of(1F))
                    ),
                    AllOfCondition.allOf(
                        WeatherCheck.weather().setThundering(true),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .located(LocationPredicate.Builder.location()
                                    .setCanSeeSky(true)
                                )
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.DIRECT_ATTACKER,
                            EntityPredicate.Builder.entity()
                                .entityType(getEntityTypePredicate(entityTypeGetter, BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES))
                        ),
                        InvertedLootItemCondition.invert(
                            MatchTool.toolMatches(itemEnchantedPredicate(enchantmentGetter, Enchantments.CHANNELING))
                        )
                    )
                )
                .ability(
                    TrimAbilityComponents.HIT_BLOCK,
                    AllOf.entityAbilities(
                        new SummonEntityAbility(EntityType.LIGHTNING_BOLT),
                        new PlaySoundAbility(SoundEvents.TRIDENT_THUNDER, ConstantFloat.of(5.0F), ConstantFloat.of(1.0F))
                    ),
                    AllOfCondition.allOf(
                        WeatherCheck.weather().setThundering(true),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .entityType(getEntityTypePredicate(entityTypeGetter, BetterTrimsEntityTypeTags.CONDUCTIVE_PROJECTILES))
                        ),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.LIGHTNING_ROD),
                        InvertedLootItemCondition.invert(
                            MatchTool.toolMatches(itemEnchantedPredicate(enchantmentGetter, Enchantments.CHANNELING))
                        )
                    )
                )
                .itemProperty(
                    TrimItemPropertyComponents.DAMAGE_IMMUNITY,
                    DamageImmunityItemProperty.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.is(DamageTypeTags.IS_LIGHTNING))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                    )
                )
                .build()
        );
        register(
            context,
            SOLAR_BONUSES,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.GOLD))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    AllOf.toggleAbilities(
                        new AttributeAbility(
                            BetterTrims.rl("trim_solar_bonus_attack_damage"),
                            Attributes.ATTACK_DAMAGE,
                            CountBasedValue.linear(1),
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_solar_bonus_movement_speed"),
                            Attributes.MOVEMENT_SPEED,
                            CountBasedValue.linear(0.07f),
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_solar_bonus_armour"),
                            Attributes.ARMOR,
                            CountBasedValue.linear(1),
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_solar_bonus_attack_speed"),
                            Attributes.ATTACK_SPEED,
                            CountBasedValue.linear(0.5f),
                            AttributeModifier.Operation.ADD_VALUE
                        )
                    ),
                    AllOfCondition.allOf(
                        TimeCheck.time(IntRange.upperBound(13000)).setPeriod(24000),
                        DimensionCheck.of(dimensionGetter.getOrThrow(BetterTrimsDimensionTypeTags.HAS_SUN))
                    )
                )
                .build()
        );
        register(
            context,
            LUNAR_BONUSES,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.SILVER))
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    AllOf.toggleAbilities(
                        new AttributeAbility(
                            BetterTrims.rl("trim_lunar_bonus_attack_damage"),
                            Attributes.ATTACK_DAMAGE,
                            CountBasedValue.linear(1),
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_lunar_bonus_movement_speed"),
                            Attributes.MOVEMENT_SPEED,
                            CountBasedValue.linear(0.07f),
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_lunar_bonus_armour"),
                            Attributes.ARMOR,
                            CountBasedValue.linear(1),
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        new AttributeAbility(
                            BetterTrims.rl("trim_lunar_bonus_attack_speed"),
                            Attributes.ATTACK_SPEED,
                            CountBasedValue.linear(0.05f),
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                    ),
                    AllOfCondition.allOf(
                        TimeCheck.time(IntRange.lowerBound(13000)).setPeriod(24000),
                        DimensionCheck.of(dimensionGetter.getOrThrow(BetterTrimsDimensionTypeTags.HAS_MOON))
                    )
                )
                .ability(
                    TrimAbilityComponents.EQUIPPED,
                    new ToggleMobEffectAbility(
                        MobEffects.NIGHT_VISION,
                        CountBasedValue.constant(0)
                    ),
                    wearingInSlot(materialGetter, TrimMaterialTags.SILVER, EquipmentSlot.HEAD)
                )
                .build()
        );
        register(
            context,
            IMPROVED_TRADING,
            TrimProperty.builder(getMaterialMatcher(materialGetter, TrimMaterialTags.EMERALD))
                .ability(
                    TrimAbilityComponents.TRADE_COST,
                    TrimValueAbility.multiply(CountBasedValue.linear(0.9f, -0.1f))
                )
                .build()
        );
    }

    private static EntityTypePredicate getEntityTypePredicate(HolderGetter<EntityType<?>> entityTypeGetter, TagKey<EntityType<?>> tag) {
        //? if 1.21.8 {
        return EntityTypePredicate.of(entityTypeGetter, tag);
        //?} elif 1.21.1 {
        /*return EntityTypePredicate.of(tag);
         *///?}
    }

    private static LootItemCondition.Builder wearingInSlot(HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material, EquipmentSlot slot) {
        return LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.THIS,
            EntityPredicate.Builder.entity().equipment(addSlot(EntityEquipmentPredicate.Builder.equipment(), materialGetter, material, slot))
        );
    }

    private static LootItemCondition.Builder wearingFullSet(HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material) {
        return LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.THIS,
            EntityPredicate.Builder.entity().equipment(fullSetMaterialPredicate(materialGetter, material))
        );
    }

    private static EntityEquipmentPredicate.Builder fullSetMaterialPredicate(HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material) {
        EntityEquipmentPredicate.Builder builder = EntityEquipmentPredicate.Builder.equipment();
        for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)) {
            addSlot(builder, materialGetter, material, slot);
        }
        return builder;
    }

    private static EntityEquipmentPredicate.Builder addSlot(EntityEquipmentPredicate.Builder builder, HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material, EquipmentSlot slot) {
        switch (slot) {
            case HEAD -> builder.head(itemMaterialPredicate(materialGetter, material));
            case CHEST -> builder.chest(itemMaterialPredicate(materialGetter, material));
            case LEGS -> builder.legs(itemMaterialPredicate(materialGetter, material));
            case FEET -> builder.feet(itemMaterialPredicate(materialGetter, material));
            case BODY -> builder.body(itemMaterialPredicate(materialGetter, material));
            case MAINHAND -> builder.mainhand(itemMaterialPredicate(materialGetter, material));
            case OFFHAND -> builder.offhand(itemMaterialPredicate(materialGetter, material));
            default -> {}
        }
        return builder;
    }

    private static ItemPredicate.Builder itemMaterialPredicate(HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material) {
        return ItemPredicate.Builder.item()
            //? if 1.21.8 {
            .withComponents(
                DataComponentMatchers.Builder.components()
                    .partial(
                        DataComponentPredicates.ARMOR_TRIM,
                        new TrimPredicate(
                            Optional.of(materialGetter.getOrThrow(material)),
                            Optional.empty()
                        )
                    )
                    .build()
            );
        //?} elif 1.21.1 {
            /*.withSubPredicate(
                ItemSubPredicates.ARMOR_TRIM,
                new ItemTrimPredicate(
                    Optional.of(materialGetter.getOrThrow(material)),
                    Optional.empty()
                )
            );
            *///?}
    }

    private static ItemPredicate.Builder itemEnchantedPredicate(HolderGetter<Enchantment> enchantmentGetter, ResourceKey<Enchantment> enchantment) {
        return ItemPredicate.Builder.item()
            //? if 1.21.8 {
            .withComponents(
                DataComponentMatchers.Builder.components()
                    .partial(
                        DataComponentPredicates.ENCHANTMENTS,
                        EnchantmentsPredicate.Enchantments.enchantments(List.of(
                            new EnchantmentPredicate(
                                enchantmentGetter.getOrThrow(enchantment),
                                MinMaxBounds.Ints.ANY
                            )
                        ))
                    )
                    .build()
            );
        //?} elif 1.21.1 {
            /*.withSubPredicate(
                ItemSubPredicates.ENCHANTMENTS,
                ItemEnchantmentsPredicate.enchantments(
                    List.of(
                        new EnchantmentPredicate(
                            enchantmentGetter.getOrThrow(enchantment),
                            MinMaxBounds.Ints.ANY
                        )
                    )
                )
            );
            *///?}
    }

    private static @NotNull Matcher getMaterialMatcher(HolderGetter<TrimMaterial> materialGetter, TagKey<TrimMaterial> material) {
        return Matcher.forMaterial(materialGetter.getOrThrow(material), 1);
    }

    static Iterable<TrimProperty> getProperties(Level level) {
        return level.registryAccess()
            .lookupOrThrow(BetterTrimsRegistries.Keys.TRIM_PROPERTIES)
            .listElements()
            .map(Holder.Reference::value)
            .toList();
    }

    private static void register(BootstrapContext<TrimProperty> context, ResourceKey<TrimProperty> key, TrimProperty property) {
        context.register(key, property);
    }

    private static ResourceKey<TrimProperty> key(String key) {
        return ResourceKey.create(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, BetterTrims.rl(key));
    }
}
