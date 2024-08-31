package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(EntityAttributes.class)
public abstract class EntityAttributesMixin {
    static {
        TrimEntityAttributes.BONUS_XP = bettertrims$registerUncappedPercentage("bonus_xp");
        TrimEntityAttributes.BOUNCY = bettertrims$registerAttribute("bouncy", 0, 0, 1, true);
        TrimEntityAttributes.BREWERS_DREAM = bettertrims$registerTrackedLeveledAttribute("brewers_dream");
        TrimEntityAttributes.CLEAVING = bettertrims$registerPercentage("cleaving");
        TrimEntityAttributes.DODGE_CHANCE = bettertrims$registerPercentage("dodge_chance");
        TrimEntityAttributes.ECHOING = bettertrims$registerLeveledAttribute("echoing");
        TrimEntityAttributes.ELECTRIFYING = bettertrims$registerTrackedLeveledAttribute("electrifying");
        TrimEntityAttributes.ENCHANTERS_FAVOUR = bettertrims$registerTrackedLeveledAttribute("enchanters_favour");
        TrimEntityAttributes.ENDS_BLESSING = bettertrims$registerLeveledAttribute("ends_blessing");
        TrimEntityAttributes.FIREY_THORNS = bettertrims$registerLeveledAttribute("firey_thorns");
        TrimEntityAttributes.FIRE_ASPECT = bettertrims$registerLeveledAttribute("fire_aspect");
        TrimEntityAttributes.FIRE_RESISTANCE = bettertrims$registerPercentage("fire_resistance");
        TrimEntityAttributes.FORTUNE = bettertrims$registerLeveledAttribute("fortune");
        TrimEntityAttributes.GLOWING = bettertrims$registerTrackedLeveledAttribute("glowing");
        TrimEntityAttributes.HELLS_BLESSING = bettertrims$registerLeveledAttribute("hells_blessing");
        TrimEntityAttributes.HYDROPHOBIC = bettertrims$registerAttribute("hydrophobic", 0, 0, 1, false, EntityAttribute.Category.NEGATIVE);
        TrimEntityAttributes.ITEM_MAGNET = bettertrims$registerLeveledAttribute("item_magnet");
        TrimEntityAttributes.LIGHT_FOOTED = bettertrims$registerLeveledAttribute("light_footed");
        TrimEntityAttributes.MINERS_RUSH = bettertrims$registerLeveledAttribute("miners_rush");
        TrimEntityAttributes.MOONS_BLESSING = bettertrims$registerTrackedLeveledAttribute("moons_blessing");
        TrimEntityAttributes.PROJECTILE_DODGE_CHANCE = bettertrims$registerPercentage("projectile_dodge_chance");
        TrimEntityAttributes.REGENERATION = bettertrims$registerAttribute("regeneration", 0, 0, 50, true);
        TrimEntityAttributes.RESISTANCE = bettertrims$registerPercentage("resistance");
        TrimEntityAttributes.SHARE_EFFECT_RADIUS = bettertrims$registerLeveledAttribute("share_effect_radius");
        TrimEntityAttributes.SUNS_BLESSING = bettertrims$registerTrackedLeveledAttribute("suns_blessing");
        TrimEntityAttributes.SWIM_SPEED = bettertrims$registerTrackedPercentage("swim_speed");
        TrimEntityAttributes.THORNS = bettertrims$registerLeveledAttribute("thorns");
        TrimEntityAttributes.TRADE_DISCOUNT = bettertrims$registerPercentage("trade_discount");
        TrimEntityAttributes.WALKING_FURNACE = bettertrims$registerLeveledAttribute("walking_furnace");

        TrimEffects.init();
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerLeveledAttribute(String id) {
        return bettertrims$registerAttribute(id, 0, 0, 4, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerTrackedLeveledAttribute(String id) {
        return bettertrims$registerAttribute(id, 0, 0, 4, true);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerPercentage(String id) {
        return bettertrims$registerAttribute(id, 1, 1, 100, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerTrackedPercentage(String id) {
        return bettertrims$registerAttribute(id, 1, 1, 100, true);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerUncappedPercentage(String id) {
        return bettertrims$registerAttribute(id, 1, 1, 2048, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerAttribute(String id, double fallback, double min, double max, boolean tracked) {
        return bettertrims$registerAttribute(id, fallback, min, max, tracked, EntityAttribute.Category.POSITIVE);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerAttribute(String id, double fallback, double min, double max, boolean tracked, EntityAttribute.Category category) {
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
