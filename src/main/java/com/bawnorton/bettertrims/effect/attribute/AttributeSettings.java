package com.bawnorton.bettertrims.effect.attribute;

import com.bawnorton.configurable.Configurable;

@Configurable("attributes")
public final class AttributeSettings {
    @Configurable("brewers_dream")
    public static class BrewersDream {
        @Configurable(value = "modification_chance", min = 0, max = 1)
        public static float modificationChance = 0.075f;
    }

    @Configurable("electrifying")
    public static class Electrifying {
        @Configurable(min = 1, max = 4)
        public static int radius = 2;
    }

    @Configurable("echoing")
    public static class Echoing {
        @Configurable(value = "echo_duration", min = 0, max = 10)
        public static int echoDuration = 5;
        @Configurable(value = "base_dampening_duration", min = 0, max = 3600)
        public static int baseDampeningDuration = 300;
        @Configurable(value = "dampening_reduction", min = 0, max = 3600)
        public static int dampeningReduction = 60;
    }

    @Configurable("fire_aspect")
    public static class FireAspect {
        @Configurable(value = "base", min = 0, max = 16)
        public static int base = 4;
        @Configurable(value = "seconds_per_level", min = 0, max = 16)
        public static int seconds = 4;
    }

    @Configurable("firey_thorns")
    public static class FireyThorns {
        @Configurable(value = "base", min = 0, max = 16)
        public static int base = 4;
        @Configurable(value = "seconds", min = 0, max = 16)
        public static int seconds = 4;
    }

    @Configurable("suns_blessing")
    public static class SunsBlessing {
        @Configurable(value = "movement_speed", min = 0, max = 1)
        public static float movementSpeed = 0.05f;
        @Configurable(value = "damage_resistance", min = 0, max = 1)
        public static float damageResistance = 0.03f;
        @Configurable(value = "attack_damage", min = 0, max = 10)
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", min = 0, max = 5)
        public static float attackSpeed = 0.3f;
    }

    @Configurable("moons_blessing")
    public static class MoonsBlessing {
        @Configurable(value = "movement_speed", min = 0, max = 1)
        public static float movementSpeed = 0.05f;
        @Configurable(value = "damage_resistance", min = 0, max = 1)
        public static float damageResistance = 0.03f;
        @Configurable(value = "attack_damage", min = 0, max = 10)
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", min = 0, max = 5)
        public static float attackSpeed = 0.3f;
    }

    @Configurable("hells_blessing")
    public static class HellsBlessing {
        @Configurable(value = "movement_speed", min = 0, max = 1)
        public static float movementSpeed = 0.05f;
        @Configurable(value = "damage_resistance", min = 0, max = 1)
        public static float damageResistance = 0.03f;
        @Configurable(value = "attack_damage", min = 0, max = 10)
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", min = 0, max = 5)
        public static float attackSpeed = 0.3f;
    }

    @Configurable("ends_blessing")
    public static class EndsBlessing {
        @Configurable(value = "movement_speed", min = 0, max = 1)
        public static float movementSpeed = 0.05f;
        @Configurable(value = "damage_resistance", min = 0, max = 1)
        public static float damageResistance = 0.03f;
        @Configurable(value = "attack_damage", min = 0, max = 10)
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", min = 0, max = 5)
        public static float attackSpeed = 0.3f;
    }

    @Configurable("enchanters_favour")
    public static class EnchantersFavour {
        @Configurable(min = 1, max = 10)
        public static int rerolls = 1;
    }

    @Configurable("item_magnet")
    public static class ItemMagnet {
        @Configurable(min = 0, max = 4)
        public static float radius;
    }

    @Configurable("light_footed")
    public static class LightFooted {
        @Configurable(value = "noise_dampening", min = 0, max = 5)
        public static float noiseDampening = 1;
    }

    @Configurable("miners_rush")
    public static class MinersRush {
        @Configurable(min = 0, max = 30)
        public static float seconds = 2.5f;
        @Configurable(value = "bonus_mine_speed", min = 0, max = 10)
        public static double bonusMineSpeed = 0.25f;
    }

    @Configurable("walking_furnace")
    public static class WalkingFurnace {
        @Configurable(min = 0, max = 64)
        public static int items = 2;
    }
}
