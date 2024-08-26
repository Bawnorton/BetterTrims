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

@Mixin(EntityAttributes.class)
public abstract class EntityAttributesMixin {
    static {
        TrimEntityAttributes.BREWERS_DREAM = bettertrims$registerTrackedLeveledAttribute("brewers_dream");
        TrimEntityAttributes.ELECTRIFYING = bettertrims$registerTrackedLeveledAttribute("electrifying");
        TrimEntityAttributes.MINERS_RUSH = bettertrims$registerLeveledAttribute("miners_rush");
        TrimEntityAttributes.FORTUNE = bettertrims$registerLeveledAttribute("fortune");
        TrimEntityAttributes.TRADE_DISCOUNT = bettertrims$registerPercentage("trade_discount");
        TrimEntityAttributes.SUNS_BLESSING = bettertrims$registerTrackedLeveledAttribute("suns_blessing");
        TrimEntityAttributes.MOONS_BLESSING = bettertrims$registerTrackedLeveledAttribute("suns_blessing");
        TrimEntityAttributes.ITEM_MAGNET = bettertrims$registerLeveledAttribute("item_magnet");
        TrimEntityAttributes.ENCHANTERS_BLESSING = bettertrims$registerTrackedLeveledAttribute("enchanters_blessing");
        TrimEntityAttributes.FIRE_RESISTANCE = bettertrims$registerPercentage("fire_resistance");
        TrimEntityAttributes.RESISTANCE = bettertrims$registerPercentage("resistance");
        TrimEntityAttributes.BONUS_XP = bettertrims$registerUncappedPercentage("bonus_xp");
        TrimEntityAttributes.WALKING_FURNACE = bettertrims$registerLeveledAttribute("walking_furnace");
        TrimEntityAttributes.SHARE_EFFECT_RADIUS = bettertrims$registerLeveledAttribute("share_effect_radius");
        TrimEntityAttributes.ECHOING = bettertrims$registerLeveledAttribute("echoing");
        TrimEntityAttributes.DODGE_CHANCE = bettertrims$registerPercentage("dodge_chance");
        TrimEntityAttributes.REGENERATION = bettertrims$registerPercentage("regeneration");

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
    private static RegistryEntry<EntityAttribute> bettertrims$registerUncappedPercentage(String id) {
        return bettertrims$registerAttribute(id, 1, 1, 2048, false);
    }

    @Unique
    private static RegistryEntry<EntityAttribute> bettertrims$registerAttribute(String id, double fallback, double min, double max, boolean tracked) {
        return Registry.registerReference(
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
}
