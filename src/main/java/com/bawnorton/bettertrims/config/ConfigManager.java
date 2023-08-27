package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.BetterTrims;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(BetterTrims.MOD_ID + ".json");

    public static void loadConfig() {
        Config config = load();

        config.trimDurability = validate(config.trimDurability, 1);
        config.quartzExperienceBonus = validate(config.quartzExperienceBonus, 0.05f);
        config.ironMiningSpeedIncrease = validate(config.ironMiningSpeedIncrease, 8f);
        config.netheriteFireResistance = validate(config.netheriteFireResistance, 0.25f);
        config.redstoneMovementSpeedIncrease = validate(config.redstoneMovementSpeedIncrease, 0.1f);
        config.copperSwimSpeedIncrease = validate(config.copperSwimSpeedIncrease, 0.05f);
        config.emeraldVillagerDiscount = validate(config.emeraldVillagerDiscount, 0.125f);
        config.diamondDamageReduction = validate(config.diamondDamageReduction, 0.05f);
        config.lapisEnchantability = validate(config.lapisEnchantability, 30);
        config.amethystPotionDurationModifyChance = validate(config.amethystPotionDurationModifyChance, 0.0625f);
        config.chorusFruitDodgeChance = validate(config.chorusFruitDodgeChance, 0.25f);
        config.fireChargeFireDuration = validate(config.fireChargeFireDuration, 1f);
        config.leatherStepHeightIncrease = validate(config.leatherStepHeightIncrease, 0.4f);
        config.dragonBreathRadius = validate(config.dragonBreathRadius, 1.25f);

        validateSilverBonus(config);
        validateSlimeBall(config);

        Config.update(config);
        save();
        BetterTrims.LOGGER.info("Loaded config");
    }

    private static void validateSilverBonus(Config config) {
        Config.SilverBonus silverBonus = config.silverNightBonus;
        if(silverBonus == null) {
            silverBonus = new Config.SilverBonus();
            config.silverNightBonus = silverBonus;
        }

        silverBonus.movementSpeed = validate(silverBonus.movementSpeed, 0.05f);
        silverBonus.jumpHeight = validate(silverBonus.jumpHeight, 0.05f);
        silverBonus.attackDamage = validate(silverBonus.attackDamage, 0.5f);
        silverBonus.attackSpeed = validate(silverBonus.attackSpeed, 0.3f);
        silverBonus.damageReduction = validate(silverBonus.damageReduction, 0.03f);
        silverBonus.improveVision = validate(silverBonus.improveVision, 0.25f);
    }

    private static void validateSlimeBall(Config config) {
        Config.SlimeBall slimeBall = config.slimeBallEffects;
        if(slimeBall == null) {
            slimeBall = new Config.SlimeBall();
            config.slimeBallEffects = slimeBall;
        }

        slimeBall.knockbackIncrease = validate(slimeBall.knockbackIncrease, 0.25f);
        slimeBall.fallDamageReduction = validate(slimeBall.fallDamageReduction, 0.25f);
    }

    private static <T extends Number> T validate(T value, T defaultValue) {
        return value == null || value.floatValue() < 0 ? defaultValue : value;
    }

    private static Config load() {
        Config config = Config.getInstance();
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                return config;
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(configPath), Config.class);
            } catch (JsonSyntaxException e) {
                BetterTrims.LOGGER.error("Failed to parse config file, using default config");
                config = Config.getInstance();
            }
        } catch (IOException e) {
            BetterTrims.LOGGER.error("Failed to load config", e);
        }
        return config == null ? Config.getInstance() : config;
    }

    private static void save() {
        try {
            Files.write(configPath, GSON.toJson(Config.getInstance()).getBytes());
        } catch (IOException e) {
            BetterTrims.LOGGER.error("Failed to save config", e);
        }
    }
}
