package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.event.PreRegistryFreezeCallback;
import com.bawnorton.bettertrims.registry.AliasedRegistryEntry;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(EntityAttributes.class)
public abstract class EntityAttributesMixin {
    static {
        TrimEntityAttributes.ATTACK_DEFLECT_CHANCE = bettertrims$registerPercentage("attack_deflect_chance");
        TrimEntityAttributes.BLAST_RESISTANCE = bettertrims$registerPercentage("blast_resistance");
        TrimEntityAttributes.BLAST_MINING = bettertrims$registerLeveled("blast_mining");
        TrimEntityAttributes.BONUS_XP = bettertrims$registerUncappedPercentage("bonus_xp");
        TrimEntityAttributes.BOUNCY = bettertrims$register("bouncy", 0, 0, 1, true);
        TrimEntityAttributes.BREWERS_DREAM = bettertrims$registerTrackedLeveled("brewers_dream");
        TrimEntityAttributes.CARMOT_SHIELD = bettertrims$registerAliased(
                "carmot_shield",
                Identifier.of("mythicmetals", "carmot_shield"),
                0,
                0,
                2048,
                true,
                EntityAttribute.Category.NEUTRAL
        );
        TrimEntityAttributes.CLEAVING = bettertrims$registerPercentage("cleaving");
        TrimEntityAttributes.DENSE = bettertrims$registerTrackedLeveled("dense");
        TrimEntityAttributes.DODGE_CHANCE = bettertrims$registerPercentage("dodge_chance");
        TrimEntityAttributes.ECHOING = bettertrims$registerLeveled("echoing");
        TrimEntityAttributes.ELECTRIFYING = bettertrims$registerTrackedLeveled("electrifying");
        TrimEntityAttributes.ELYTRA_ROCKET_SPEED = bettertrims$registerAliased(
                "elytra_rocket_speed",
                Identifier.of("mythicmetals", "elytra_rocket_speed"),
                1,
                0,
                1024,
                true,
                EntityAttribute.Category.NEUTRAL
        );
        TrimEntityAttributes.ENCHANTERS_FAVOUR = bettertrims$registerTrackedLeveled("enchanters_favour");
        TrimEntityAttributes.ENDS_BLESSING = bettertrims$registerLeveled("ends_blessing");
        TrimEntityAttributes.FIREY_THORNS = bettertrims$registerLeveled("firey_thorns");
        TrimEntityAttributes.FIRE_ASPECT = bettertrims$registerLeveled("fire_aspect");
        TrimEntityAttributes.FIRE_RESISTANCE = bettertrims$registerPercentage("fire_resistance");
        TrimEntityAttributes.FORTUNE = bettertrims$registerLeveled("fortune");
        TrimEntityAttributes.GLOWING = bettertrims$registerTrackedLeveled("glowing");
        TrimEntityAttributes.HELLS_BLESSING = bettertrims$registerLeveled("hells_blessing");
        TrimEntityAttributes.HOLY = bettertrims$registerLeveled("holy");
        //? if >=1.21 {
        TrimEntityAttributes.HYDROPHOBIC = bettertrims$register("hydrophobic", 0, 0, 1, false, EntityAttribute.Category.NEGATIVE);
        //?} else {
        /*TrimEntityAttributes.HYDROPHOBIC = bettertrims$register("hydrophobic", 0, 0, 1, false);
        *///?}
        TrimEntityAttributes.ITEM_MAGNET = bettertrims$registerLeveled("item_magnet");
        TrimEntityAttributes.LAVA_MOVEMENT_SPEED = bettertrims$registerAliased(
                "lava_movement_speed",
                Identifier.of("additionalentityattributes", "generic.lava_speed"),
                0.5,
                0,
                1,
                true,
                EntityAttribute.Category.NEUTRAL
        );
        TrimEntityAttributes.LAVA_VISIBILITY = bettertrims$registerAliased(
                "lava_visibility",
                Identifier.of("additionalentityattributes", "player.lava_visibility"),
                1,
                0,
                1024,
                true,
                EntityAttribute.Category.NEUTRAL
        );
        TrimEntityAttributes.LIGHT_FOOTED = bettertrims$registerLeveled("light_footed");
        TrimEntityAttributes.LOOTING = bettertrims$registerLeveled("looting");
        TrimEntityAttributes.MAGIC_PROTECTION = bettertrims$registerAliased(
                "magic_protection",
                Identifier.of("additionalentityattributes", "generic.magic_protection"),
                0,
                0,
                1024,
                true,
                EntityAttribute.Category.NEUTRAL
        );
        TrimEntityAttributes.MIDAS_TOUCH = bettertrims$register("midas_touch", 0, 0, 1, true, EntityAttribute.Category.NEGATIVE);
        TrimEntityAttributes.MINERS_RUSH = bettertrims$registerLeveled("miners_rush");
        TrimEntityAttributes.MOONS_BLESSING = bettertrims$registerTrackedLeveled("moons_blessing");
        TrimEntityAttributes.OVERGROWN = bettertrims$registerLeveled("overgrown");
        TrimEntityAttributes.PROJECTILE_DAMAGE = bettertrims$register("projectile_damage", 0, 0, 2048, false);
        TrimEntityAttributes.PROJECTILE_DODGE_CHANCE = bettertrims$registerPercentage("projectile_dodge_chance");
        TrimEntityAttributes.PROJECTILE_DEFLECT_CHANCE = bettertrims$registerPercentage("projectile_deflect_chance");
        TrimEntityAttributes.PROJECTILE_SPEED = bettertrims$registerUncappedPercentage("projectile_speed");
        TrimEntityAttributes.REGENERATION = bettertrims$register("regeneration", 0, 0, 50, true);
        TrimEntityAttributes.RESISTANCE = bettertrims$registerPercentage("resistance");
        TrimEntityAttributes.SHARE_EFFECT_RADIUS = bettertrims$registerLeveled("share_effect_radius");
        TrimEntityAttributes.SUNS_BLESSING = bettertrims$registerTrackedLeveled("suns_blessing");
        TrimEntityAttributes.SWIM_SPEED = bettertrims$registerTrackedPercentage("swim_speed");
        TrimEntityAttributes.THORNS = bettertrims$registerLeveled("thorns");
        TrimEntityAttributes.TRADE_DISCOUNT = bettertrims$registerPercentage("trade_discount");
        TrimEntityAttributes.UNBREAKING = bettertrims$registerLeveled("unbreaking");
        TrimEntityAttributes.WALKING_FURNACE = bettertrims$registerLeveled("walking_furnace");
        TrimEntityAttributes.WARRIORS_OF_OLD = bettertrims$registerLeveled("warriors_of_old");
        //? if <1.21 {
        /*TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED = bettertrims$register("player_block_break_speed", 1, 0, 1024, true);
        TrimEntityAttributes.GENERIC_STEP_HEIGHT = bettertrims$register("generic_step_height", 0.0, 0.0, 10.0, true);
        TrimEntityAttributes.GENERIC_OXYGEN_BONUS = bettertrims$register("generic_oxygen_bonus", 0.0, 0.0, 1024.0, true);
        *///?}

        TrimEffects.init();
    }

    //? if >=1.21 {
    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerLeveled(String id) {
        return bettertrims$register(id, 0, 0, 1024, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerTrackedLeveled(String id) {
        return bettertrims$register(id, 0, 0, 1024, true);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerPercentage(String id) {
        return bettertrims$register(id, 1, 1, 100, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerTrackedPercentage(String id) {
        return bettertrims$register(id, 1, 1, 100, true);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerUncappedPercentage(String id) {
        return bettertrims$register(id, 1, 1, 2048, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$register(String id, double fallback, double min, double max, boolean tracked) {
        return bettertrims$register(id, fallback, min, max, tracked, EntityAttribute.Category.POSITIVE);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$register(String id, double fallback, double min, double max, boolean tracked, EntityAttribute.Category category) {
        return Registry.registerReference(
                Registries.ATTRIBUTE,
                BetterTrims.id(id),
                new ClampedEntityAttribute(
                        "bettertrims.attribute.name.%s".formatted(id),
                        fallback,
                        min,
                        max
                ).setTracked(tracked).setCategory(category)
        );
    }

    @Unique
    private static AliasedRegistryEntry<EntityAttribute> bettertrims$registerAliased(String id, Identifier alias, double fallback, double min, double max, boolean tracked, EntityAttribute.Category category) {
        AliasedRegistryEntry<EntityAttribute> aliasedRegEntry = new AliasedRegistryEntry<>();
        PreRegistryFreezeCallback.EVENT.register(registry -> {
            if (registry == Registries.ATTRIBUTE) {
                aliasedRegEntry.init(Registries.ATTRIBUTE.getEntry(alias)
                        .map(entry -> new AliasedRegistryEntry.EntryHolder<>(entry, true))
                        .orElse(new AliasedRegistryEntry.EntryHolder<>(bettertrims$register(id, fallback, min, max, tracked, category), false)));
            }
        });
        return aliasedRegEntry;
    }

    //?} else {
    /*@Unique
    private static EntityAttribute bettertrims$registerLeveled(String id) {
        return bettertrims$register(id, 0, 0, 4, false);
    }

    @Unique
    private static EntityAttribute bettertrims$registerTrackedLeveled(String id) {
        return bettertrims$register(id, 0, 0, 4, true);
    }

    @Unique
    private static EntityAttribute bettertrims$registerPercentage(String id) {
        return bettertrims$register(id, 1, 1, 100, false);
    }

    @Unique
    private static EntityAttribute bettertrims$registerTrackedPercentage(String id) {
        return bettertrims$register(id, 1, 1, 100, true);
    }

    @Unique
    private static EntityAttribute bettertrims$registerUncappedPercentage(String id) {
        return bettertrims$register(id, 1, 1, 2048, false);
    }

    @Unique
    private static EntityAttribute bettertrims$register(String id, double fallback, double min, double max, boolean tracked) {
        return Registry.register(
                Registries.ATTRIBUTE,
                BetterTrims.id(id),
                new ClampedEntityAttribute(
                        "bettertrims.attribute.name.%s".formatted(id),
                        fallback,
                        min,
                        max
                ).setTracked(tracked)
        );
    }
    *///?}

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/attribute/ClampedEntityAttribute;<init>(Ljava/lang/String;DDD)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=generic.knockback_resistance"
                    )
            ),
            index = 2
    )
    private static double allowNegativeKnockbackRes(double min) {
        return -16;
    }
}
