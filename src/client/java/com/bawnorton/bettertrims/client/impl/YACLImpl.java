package com.bawnorton.bettertrims.client.impl;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.config.option.ConfigOptionReference;
import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.OptionType;
import com.bawnorton.bettertrims.config.option.annotation.NestedOption;
import com.bawnorton.bettertrims.util.Reflection;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public abstract class YACLImpl {
    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(title("main"))
                .category(ConfigCategory.createBuilder()
                        .name(category("general"))
                        .tooltip(tooltip("general"))
                        .group(OptionGroup.createBuilder()
                                .name(group("game"))
                                .description(OptionDescription.of(description("game")))
                                .options(generateOptionsForType(OptionType.GAME))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(group("vanilla"))
                                .description(OptionDescription.of(description("vanilla")))
                                .options(generateOptionsForType(OptionType.VANILLA))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(group("added.vanilla"))
                                .description(OptionDescription.of(description("added.vanilla")))
                                .options(generateOptionsForType(OptionType.ADDED_VANILLA))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(group("modded"))
                                .description(OptionDescription.of(description("modded")))
                                .options(generateOptionsForType(OptionType.MODDED))
                                .build())
                        .build())
                .save(() -> {
                    ConfigManager.saveConfig();
                    boolean inWorld = MinecraftClient.getInstance().world != null;
                    if(inWorld) ClientNetworking.trySendConfigToServer();
                })
                .build()
                .generateScreen(parent);
    }

    private static Collection<? extends Option<?>> generateOptionsForType(OptionType type) {
        Collection<Option<?>> options = new HashSet<>();
        Reflection.forEachAnnotatedField(Config.getInstance(), field -> {
            ConfigOptionReference reference = ConfigOptionReference.of(field);
            if(reference.notOf(type)) return;

            if(field.isAnnotationPresent(NestedOption.class)) {
                options.addAll(createNestedOptions(reference, type));
            } else {
                options.add(createOption(reference));
            }
        });
        return options;
    }

    private static Collection<? extends Option<?>> createNestedOptions(ConfigOptionReference reference, OptionType type) {
        Collection<Option<?>> options = new ArrayList<>();
        NestedConfigOption instance = reference.nestedValue();
        Reflection.forEachAnnotatedField(instance, nestedField -> {
            ConfigOptionReference nestedReference = ConfigOptionReference.of(nestedField);
            if(nestedReference.notOf(type)) return;

            if(nestedField.isAnnotationPresent(NestedOption.class)) {
                options.addAll(createNestedOptions(nestedReference, type));
            } else {
                options.add(createOption(nestedReference));
            }
        });
        return options;
    }

    private static Option<?> createOption(ConfigOptionReference reference) {
        return switch (reference.getType()) {
            case BOOLEAN -> booleanOption(reference);
            case INTEGER -> integerOption(reference);
            case FLOAT -> floatOption(reference);
            case NESTED -> throw new IllegalArgumentException("Nested options should be handled by createNestedOptions");
        };
    }

    private static Option<Float> floatOption(ConfigOptionReference reference) {
        String formattedName = reference.getFormattedName();
        return Option.<Float>createBuilder()
                .name(option(formattedName))
                .description(OptionDescription.of(description(formattedName)))
                .binding(Binding.generic(reference.floatValue(), reference::floatValue, reference::floatValue))
                .controller(option -> FloatSliderControllerBuilder
                        .create(option)
                        .range(reference.minFloatValue(), reference.maxFloatValue()))
                .listener((option, value) -> reference.floatValue(value))
                .build();
    }

    private static Option<Boolean> booleanOption(ConfigOptionReference reference) {
        String formattedName = reference.getFormattedName();
        return Option.<Boolean>createBuilder()
                .name(option(formattedName))
                .description(OptionDescription.of(description(formattedName)))
                .binding(Binding.generic(reference.booleanValue(), reference::booleanValue, reference::booleanValue))
                .controller(TickBoxControllerBuilder::create)
                .listener((option, value) -> reference.booleanValue(value))
                .build();
    }

    private static Option<Integer> integerOption(ConfigOptionReference reference) {
        String formattedName = reference.getFormattedName();
        return Option.<Integer>createBuilder()
                .name(option(formattedName))
                .description(OptionDescription.of(description(formattedName)))
                .binding(Binding.generic(reference.intValue(), reference::intValue, reference::intValue))
                .controller(option -> IntegerSliderControllerBuilder
                        .create(option)
                        .range(reference.minIntValue(), reference.maxIntValue()))
                .listener((option, value) -> reference.intValue(value))
                .build();
    }

    private static Text title(String path) {
        return Text.translatable("%s.yacl.title.%s", BetterTrims.MOD_ID, path);
    }

    private static Text category(String path) {
        return Text.translatable("%s.yacl.category.%s", BetterTrims.MOD_ID, path);
    }

    private static Text tooltip(String path) {
        return Text.translatable("%s.yacl.tooltip.%s", BetterTrims.MOD_ID, path);
    }

    private static Text group(String path) {
        return Text.translatable("%s.yacl.group.%s", BetterTrims.MOD_ID, path);
    }

    private static Text description(String path) {
        return Text.translatable("%s.yacl.description.%s", BetterTrims.MOD_ID, path);
    }

    private static Text option(String path) {
        return Text.translatable("%s.yacl.option.%s", BetterTrims.MOD_ID, path);
    }
}
