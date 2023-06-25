package com.bawnorton.config;

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
                '}';
    }
}
