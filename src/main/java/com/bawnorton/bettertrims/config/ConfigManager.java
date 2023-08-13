package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.BetterTrims;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(BetterTrims.MOD_ID + ".json");

    public static void loadConfig() {
        Config config = load();

        if (config.trimDurability == null || config.trimDurability < 0)
            config.trimDurability = 1;
        if (config.quartzExperienceBonus == null || config.quartzExperienceBonus < 0)
            config.quartzExperienceBonus = 0.05f;
        if (config.ironMiningSpeedIncrease == null || config.ironMiningSpeedIncrease < 0)
            config.ironMiningSpeedIncrease = 8f;
        if (config.netheriteFireResistance == null || config.netheriteFireResistance < 0)
            config.netheriteFireResistance = 0.25f;
        if (config.redstoneMovementSpeedIncrease == null || config.redstoneMovementSpeedIncrease < 0)
            config.redstoneMovementSpeedIncrease = 0.1f;
        if (config.copperSwimSpeedIncrease == null || config.copperSwimSpeedIncrease < 0)
            config.copperSwimSpeedIncrease = 0.05f;
        if (config.emeraldVillagerDiscount == null || config.emeraldVillagerDiscount < 0)
            config.emeraldVillagerDiscount = 0.125f;
        if (config.diamondDamageReduction == null || config.diamondDamageReduction < 0)
            config.diamondDamageReduction = 0.05f;
        if (config.lapisEnchantability == null || config.lapisEnchantability < 0)
            config.lapisEnchantability = 30;
        if (config.amethystEffectChance == null || config.amethystEffectChance < 0)
            config.amethystEffectChance = 0.0625f;

        validateSilverBonus(config);

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
        if (silverBonus.movementSpeed == null || silverBonus.movementSpeed < 0)
            silverBonus.movementSpeed = 0.05f;
        if (silverBonus.jumpHeight == null || silverBonus.jumpHeight < 0)
            silverBonus.jumpHeight = 0.05f;
        if (silverBonus.attackDamage == null || silverBonus.attackDamage < 0)
            silverBonus.attackDamage = 0.5f;
        if (silverBonus.attackSpeed == null || silverBonus.attackSpeed < 0)
            silverBonus.attackSpeed = 0.3f;
        if (silverBonus.damageReduction == null || silverBonus.damageReduction < 0)
            silverBonus.damageReduction = 0.03f;
        if (silverBonus.improveVision == null || silverBonus.improveVision < 0)
            silverBonus.improveVision = 0.25f;
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
