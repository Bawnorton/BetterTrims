package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.config.option.annotation.BooleanOption;
import com.bawnorton.bettertrims.config.option.annotation.FloatOption;
import com.bawnorton.bettertrims.config.option.annotation.IntOption;
import com.bawnorton.bettertrims.config.option.annotation.NestedOption;
import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.OptionType;
import com.google.gson.annotations.Expose;

public class Config {
    private static Config INSTANCE;

    @Expose @IntOption(type = OptionType.GAME, value = 1)
    public Integer trimDurability;

    @Expose @FloatOption(value = 0.05f, max = 1)
    public Float quartzExperienceBonus;
    @Expose @FloatOption(value = 8f, max = 80)
    public Float ironMiningSpeedIncrease;
    @Expose @FloatOption(value = 0.25f, max = 1)
    public Float netheriteFireResistance;
    @Expose @FloatOption(value = 0.1f, max = 1)
    public Float redstoneMovementSpeedIncrease;
    @Expose @FloatOption(value = 0.05f, max = 0.5f)
    public Float copperSwimSpeedIncrease;
    @Expose @FloatOption(value = 0.125f, max = 1)
    public Float emeraldVillagerDiscount;
    @Expose @FloatOption(value = 0.05f, max = 1)
    public Float diamondDamageReduction;
    @Expose @IntOption(value = 30)
    public Integer lapisEnchantability;
    @Expose @FloatOption(value = 0.0625f, max = 1)
    public Float amethystPotionDurationModifyChance;

    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    public Float glowstonePotionAmplifierIncreaseChance;
    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    public Float chorusFruitDodgeChance;
    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 1f, max = 10)
    public Float fireChargeFireDuration;
    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.4f, max = 4)
    public Float leatherStepHeightIncrease;
    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.25f, max = 5)
    public Float dragonBreathRadius;
    @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.5f, max = 5)
    public Float echoShardVibrationDistanceReduction;

    @Expose @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnchantedGoldenApple enchantedGoldenAppleEffects;
    @Expose @NestedOption(type = OptionType.MODDED)
    public Silver silverNightBonus;
    @Expose @NestedOption(type = OptionType.ADDED_VANILLA)
    public SlimeBall slimeBallEffects;
    @Expose @NestedOption
    public Coal coalEffects;
    @Expose @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnderPearl enderPearlEffects;

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }

    public static class EnchantedGoldenApple implements NestedConfigOption {
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 1200, max = 12000, min = 1)
        public Float absorptionDelay;
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 250, max = 12000)
        public Float absorptionDelayReduction;
        @Expose @IntOption(type = OptionType.ADDED_VANILLA, value = 2, max = 20)
        public Integer absorptionAmount;
        @Expose @IntOption(type = OptionType.ADDED_VANILLA, value = 3, max = 30)
        public Integer maxAbsorption;
    }

    public static class Silver implements NestedConfigOption {
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.05f, max = 0.5f)
        public Float movementSpeed;
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.05f, max = 0.5f)
        public Float jumpHeight;
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.5f, max = 5)
        public Float attackDamage;
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.3f, max = 3)
        public Float attackSpeed;
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.03f, max = 0.3f)
        public Float damageReduction;
        @Expose @FloatOption(type = OptionType.MODDED, value = 0.25f, max = 1)
        public Float improveVision;
    }

    public static class SlimeBall implements NestedConfigOption {
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
        public Float fallDamageReduction;
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 2.5f)
        public Float knockbackIncrease;
    }

    public static class Coal implements NestedConfigOption {
        @Expose @BooleanOption(type = OptionType.ADDED_VANILLA, value = false)
        public Boolean disableEffectToReduceLag;
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 5f, max = 10f)
        public Float playerDetectionRadius;
        @Expose @IntOption(type = OptionType.ADDED_VANILLA, value = 1, max = 10)
        public Integer furnaceSpeedMultiplier;
    }

    public static class EnderPearl implements NestedConfigOption {
        @Expose @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
        public Float dodgeChance;
        @Expose @BooleanOption(type = OptionType.ADDED_VANILLA, value = true)
        public Boolean waterDamagesUser;
    }
}
