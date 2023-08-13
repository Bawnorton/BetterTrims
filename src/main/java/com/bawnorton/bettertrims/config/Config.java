package com.bawnorton.bettertrims.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {
    private static Config INSTANCE;
    @Expose
    @SerializedName("trim_durability")
    public Integer trimDurability;
    @Expose
    @SerializedName("quartz_experience_bonus")
    public Float quartzExperienceBonus;
    @Expose
    @SerializedName("iron_mining_speed_increase")
    public Float ironMiningSpeedIncrease;
    @Expose
    @SerializedName("netherite_fire_resistance")
    public Float netheriteFireResistance;
    @Expose
    @SerializedName("redstone_movement_speed_increase")
    public Float redstoneMovementSpeedIncrease;
    @Expose
    @SerializedName("copper_swim_speed_increase")
    public Float copperSwimSpeedIncrease;
    @Expose
    @SerializedName("emerald_villager_discount")
    public Float emeraldVillagerDiscount;
    @Expose
    @SerializedName("diamond_damage_reduction")
    public Float diamondDamageReduction;
    @Expose
    @SerializedName("lapis_enchantability")
    public Integer lapisEnchantability;
    @Expose
    @SerializedName("amethyst_potion_duration_modify_chance")
    public Float amethystEffectChance;
    @Expose
    @SerializedName("silver_night_bonus")
    public SilverBonus silverNightBonus;

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }

    @Override
    public String toString() {
        return "Config{" +
                "trimDurability=" + trimDurability +
                ", quartzExperienceBonus=" + quartzExperienceBonus +
                ", ironMiningSpeedIncrease=" + ironMiningSpeedIncrease +
                ", netheriteFireResistance=" + netheriteFireResistance +
                ", redstoneMovementSpeedIncrease=" + redstoneMovementSpeedIncrease +
                ", copperSwimSpeedIncrease=" + copperSwimSpeedIncrease +
                ", emeraldVillagerDiscount=" + emeraldVillagerDiscount +
                ", diamondDamageReduction=" + diamondDamageReduction +
                ", lapisEnchantability=" + lapisEnchantability +
                ", amethystEffectChance=" + amethystEffectChance +
                ", silverNightBonus=" + silverNightBonus +
                '}';
    }

    public static class SilverBonus {
        @Expose
        @SerializedName("movement_speed")
        public Float movementSpeed;
        @Expose
        @SerializedName("jump_height")
        public Float jumpHeight;
        @Expose
        @SerializedName("attack_damage")
        public Float attackDamage;
        @Expose
        @SerializedName("attack_speed")
        public Float attackSpeed;
        @Expose
        @SerializedName("damage_reduction")
        public Float damageReduction;
        @Expose
        @SerializedName("improve_vision")
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
}
