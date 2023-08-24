package com.bawnorton.bettertrims.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    public Float amethystPotionDurabilityModifyChance;
    @Expose
    public Float chorusFruitDodgeChance;
    @Expose
    public Float fireChargeFireDuration;
    @Expose
    public Float leatherStepHeightIncrease;
    @Expose
    public SilverBonus silverNightBonus;
    @Expose
    public SlimeBall slimeBallEffects;


    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }

    public static class SilverBonus {
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

        @Override
        public String toString() {
            return "SilverBonus{" +
                    "movementSpeed=" + movementSpeed +
                    ", jumpHeight=" + jumpHeight +
                    ", attackDamage=" + attackDamage +
                    ", attackSpeed=" + attackSpeed +
                    ", damageReduction=" + damageReduction +
                    ", improveVision=" + improveVision +
                    '}';
        }
    }

    public static class SlimeBall {
        @Expose
        public Float fallDamageReduction;

        @Expose
        public Float knockbacIncrease;
    }
}
