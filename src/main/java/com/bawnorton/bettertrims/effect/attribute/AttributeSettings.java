package com.bawnorton.bettertrims.effect.attribute;

import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.ControllerType;
import com.bawnorton.configurable.Yacl;

@Configurable("attributes")
public final class AttributeSettings {
    @Configurable("brewers_dream")
    public static class BrewersDream {
        @Configurable(value = "modification_chance", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float modificationChance = 0.075f;
    }

    @Configurable("electrifying")
    public static class Electrifying {
        @Configurable(min = 1, max = 4)
        public static int radius = 2;
    }

    @Configurable("echoing")
    public static class Echoing {
        @Configurable(value = "echo_duration", max = 10)
        public static int echoDuration = 5;
        @Configurable(value = "base_dampening_duration", max = 3600)
        public static int baseDampeningDuration = 300;
        @Configurable(value = "dampening_reduction", max = 3600)
        public static int dampeningReduction = 60;
    }

    @Configurable("fire_aspect")
    public static class FireAspect {
        @Configurable(value = "base", max = 16)
        public static int base = 4;
        @Configurable(value = "seconds_per_level", max = 16)
        public static int seconds = 4;
    }

    @Configurable("firey_thorns")
    public static class FireyThorns {
        @Configurable(value = "base", max = 16)
        public static int base = 4;
        @Configurable(value = "seconds_per_level", max = 16)
        public static int seconds = 4;
    }

    @Configurable("suns_blessing")
    public static class SunsBlessing {
        @Configurable(value = "movement_speed", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float movementSpeed = 0.03f;
        @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float resistance = 0.03f;
        @Configurable(value = "attack_damage", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", max = 5, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackSpeed = 0.3f;
    }

    @Configurable("moons_blessing")
    public static class MoonsBlessing {
        @Configurable(value = "movement_speed", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float movementSpeed = 0.03f;
        @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float resistance = 0.03f;
        @Configurable(value = "attack_damage", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", max = 5, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackSpeed = 0.3f;
    }

    @Configurable("hells_blessing")
    public static class HellsBlessing {
        @Configurable(value = "movement_speed", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float movementSpeed = 0.03f;
        @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float resistance = 0.03f;
        @Configurable(value = "attack_damage", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", max = 5, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackSpeed = 0.3f;
    }

    @Configurable("ends_blessing")
    public static class EndsBlessing {
        @Configurable(value = "movement_speed", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float movementSpeed = 0.03f;
        @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float resistance = 0.03f;
        @Configurable(value = "attack_damage", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float attackDamage = 0.5f;
        @Configurable(value = "attack_speed", max = 5, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
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
        public static float radius = 1;
    }

    @Configurable("light_footed")
    public static class LightFooted {
        @Configurable(value = "noise_dampening", max = 5)
        public static float noiseDampening = 1;
    }

    @Configurable("miners_rush")
    public static class MinersRush {
        @Configurable(value = "seconds_per_level", max = 30)
        public static float secondsPerLevel = 2.5f;
        @Configurable(value = "bonus_mine_speed", max = 10)
        public static double bonusMineSpeed = 0.25f;
    }

    @Configurable("walking_furnace")
    public static class WalkingFurnace {
        @Configurable(value = "items_to_smelt", max = 64)
        public static int itemsToSmelt = 2;
    }

    @Configurable("overgrown")
    public static class Overgrown {
        @Configurable(value = "chance_to_repair", max = 1, yacl = @Yacl(controller = ControllerType.FLOAT_FIELD, formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#threeDpFormatter"))
        public static float chanceToRepair = 0.005f;
    }

    @Configurable("holy")
    public static class Holy {
        @Configurable(max = 15, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
        public static float damage = 2.5f;
    }

    @Configurable("warriors_of_old")
    public static class WarriorsOfOld {
        @Configurable(value = "armour_strength", max = 50)
        public static int armourStrength = 25;
        @Configurable(value = "min_weapon_damage", min = 3, max = 25)
        public static int minWeaponDamage = 3;
        @Configurable(value = "max_weapon_damage", min = 3, max = 25)
        public static int maxWeaponDamage = 7;
        @Configurable(value = "weapon_enchant_level", min = 0, max = 50)
        public static int weaponEnchantLevel = 18;
        @Configurable(value = "armour_enchant_level", min = 0, max = 50)
        public static int armourEnchantLevel = 25;
        @Configurable(value = "decay_rate", min = 0)
        public static int decayRate = 90;
    }
}
