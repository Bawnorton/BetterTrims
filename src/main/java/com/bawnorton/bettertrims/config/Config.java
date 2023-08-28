package com.bawnorton.bettertrims.config;

import com.google.gson.annotations.Expose;

public class Config {
    private static Config INSTANCE;
    @Expose
    public Integer trimDurability;
    @Expose
    public Float quartzExperienceBonus;
    @Expose
    public Float ironMiningSpeedIncrease;
    @Expose
    public Float netheriteFireResistance;
    @Expose
    public Float redstoneMovementSpeedIncrease;
    @Expose
    public Float copperSwimSpeedIncrease;
    @Expose
    public Float emeraldVillagerDiscount;
    @Expose
    public Float diamondDamageReduction;
    @Expose
    public Integer lapisEnchantability;
    @Expose
    public Float amethystPotionDurationModifyChance;
    @Expose
    public Float glowstonePotionAmplifierIncreaseChance;
    @Expose
    public Float chorusFruitDodgeChance;
    @Expose
    public Float fireChargeFireDuration;
    @Expose
    public Float leatherStepHeightIncrease;
    @Expose
    public Float dragonBreathRadius;
    @Expose
    public Float echoShardVibrationDistanceReduction;
    @Expose
    public EnchantedGoldenApple enchantedGoldenAppleEffects;
    @Expose
    public Silver silverNightBonus;
    @Expose
    public SlimeBall slimeBallEffects;
    @Expose
    public Coal coalEffects;

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }

    public static class EnchantedGoldenApple {
        @Expose
        public Float absorptionDelay;
        @Expose
        public Float absorptionDelayReduction;
        @Expose
        public Integer absorptionAmount;
        @Expose
        public Integer maxAbsorption;
    }

    public static class Silver {
        @Expose
        public Float movementSpeed;
        @Expose
        public Float jumpHeight;
        @Expose
        public Float attackDamage;
        @Expose
        public Float attackSpeed;
        @Expose
        public Float damageReduction;
        @Expose
        public Float improveVision;
    }

    public static class SlimeBall {
        @Expose
        public Float fallDamageReduction;

        @Expose
        public Float knockbackIncrease;
    }

    public static class Coal {
        @Expose
        public Boolean disableEffectToReduceLag;
        @Expose
        public Float playerDetectionRadius;
        @Expose
        public Integer furnaceSpeedMultiplier;
    }
}
