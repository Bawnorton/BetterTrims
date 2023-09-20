package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.config.option.reference.ConfigOptionReference;
import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.annotation.BooleanOption;
import com.bawnorton.bettertrims.config.option.annotation.FloatOption;
import com.bawnorton.bettertrims.config.option.annotation.IntOption;
import com.bawnorton.bettertrims.config.option.annotation.NestedOption;
import com.bawnorton.bettertrims.util.Reflection;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(BetterTrims.MOD_ID + ".json");

    public static void loadConfig() {
        Config config = load();

        validateFields(config);

        Config.update(config);
        save();
        BetterTrims.LOGGER.info("Loaded config");
    }

    private static void validateFields(Object instance) {
        validateFloatFields(instance);
        validateIntFields(instance);
        validateBooleanFields(instance);
        validateNestedFields(instance);
    }

    private static void validateFloatFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, FloatOption.class, (field, annotation) -> {
            validateFloatField(instance, field, annotation.value());
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            if(reference.floatValue() < annotation.min()) reference.floatValue(annotation.min());
            if(reference.floatValue() > annotation.max()) reference.floatValue(annotation.max());
        });
    }

    private static void validateIntFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, IntOption.class, (field, annotation) -> {
            validateIntField(instance, field, annotation.value());
            ConfigOptionReference reference = ConfigOptionReference.of(instance, field);
            if(reference.intValue() < annotation.min()) reference.intValue(annotation.min());
            if(reference.intValue() > annotation.max()) reference.intValue(annotation.max());
        });
    }

    private static void validateBooleanFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, BooleanOption.class, (field, annotation) -> validateBooleanField(instance, field, annotation.value()));
    }

    private static void validateNestedFields(Object instance) {
        Reflection.forEachFieldByAnnotation(instance, NestedOption.class, (field, annotation) -> {
            validateNestedField(instance, field);

            NestedConfigOption nestedOption = Reflection.accessField(field, instance, NestedConfigOption.class);
            validateFields(nestedOption);
        });
    }

    private static void validateFloatField(Object instance, Field field, Float fallback) {
        if(Reflection.accessField(field, instance) != null) return;

        Reflection.setField(field, instance, fallback);
    }

    private static void validateIntField(Object instance, Field field, Integer fallback) {
        if(Reflection.accessField(field, instance) != null) return;

        Reflection.setField(field, instance, fallback);
    }

    private static void validateBooleanField(Object instance, Field field, Boolean fallback) {
        if(Reflection.accessField(field, instance) != null) return;

        Reflection.setField(field, instance, fallback);
    }

    private static void validateNestedField(Object instance, Field field) {
        if(Reflection.accessField(field, instance) != null) return;

        Reflection.setField(field, instance, Reflection.newInstance(field.getType()));
    }

    public static String serializeConfig() {
        return GSON.toJson(Config.getInstance());
    }

    public static void deserializeConfig(String serialized) {
        Config.update(GSON.fromJson(serialized, Config.class));
        save();
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

    public static void saveConfig() {
        save();
        BetterTrims.LOGGER.info("Saved config");
    }
}
