package com.bawnorton.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {
    private static Config INSTANCE;
    @Expose
    @SerializedName("quartz_experience_bonus")
    public Float quartzExperienceBonus = 0.05f;
    @Expose
    @SerializedName("iron_mining_speed_increase")
    public Float ironMiningSpeedIncrease = 8f;
    @Expose
    @SerializedName("netherite_fire_resistance")
    public Float netheriteFireResistance = 0.25f;
    @Expose
    @SerializedName("redstone_movement_speed_increase")
    public Float redstoneMovementSpeedIncrease = 0.1f;
    @Expose
    @SerializedName("copper_swim_speed_increase")
    public Float copperSwimSpeedIncrease = 0.05f;
    @Expose
    @SerializedName("emerald_villager_discount")
    public Float emeraldVillagerDiscount = 0.125f;
    @Expose
    @SerializedName("diamond_damage_reduction")
    public Float diamondDamageReduction = 0.05f;
    @Expose
    @SerializedName("lapis_enchantability")
    public Integer lapisEnchantability = 30;
    @Expose
    @SerializedName("amethyst_potion_duration_modify_chance")
    public Float amethystEffectChance = 0.0625f;

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }
}
