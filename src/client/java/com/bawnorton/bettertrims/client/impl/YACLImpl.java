package com.bawnorton.bettertrims.client.impl;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.OptionType;
import com.bawnorton.bettertrims.config.option.reference.ConfigOptionReference;
import com.bawnorton.bettertrims.config.option.reference.ParentedConfigOptionReference;
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
            ConfigOptionReference reference = ConfigOptionReference.of(Config.getInstance(), field);
            if(reference.notOf(type)) return;

            if(reference.isNested()) {
                options.addAll(createNestedOptions(reference, type));
            } else {
                options.add(createOption(reference));
            }
        });
        return options;
    }

    private static Collection<? extends Option<?>> createNestedOptions(ConfigOptionReference reference, OptionType type) {
        if(!reference.isNested()) throw new IllegalArgumentException("Reference \"%s\" is not nested".formatted(reference.getFormattedName()));

        Collection<Option<?>> options = new ArrayList<>();
        NestedConfigOption instance = reference.nestedValue();
        Reflection.forEachAnnotatedField(instance, nestedField -> {
            ParentedConfigOptionReference parentedReference = ParentedConfigOptionReference.of(reference, instance, nestedField);
            if(parentedReference.notOf(type)) return;

            if(parentedReference.isNested()) {
                options.addAll(createNestedOptions(parentedReference, type));
            } else {
                options.add(createOption(parentedReference));
            }
        });
        return options;
    }

    private static Option<?> createOption(ConfigOptionReference reference) {
        return switch (reference.getType()) {
            case BOOLEAN -> booleanOption(reference);
            case INTEGER -> integerOption(reference);
            case FLOAT -> floatOption(reference);
            case NESTED -> throw new IllegalArgumentException("Attempted to of non-nested option for nested reference \"%s\"".formatted(reference.getFormattedName()));
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
                        .range(reference.minFloatValue(), reference.maxFloatValue())
                        .step(reference.maxFloatValue() / 100f))
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
                        .range(reference.minIntValue(), reference.maxIntValue())
                        .step(Math.max(1, reference.maxIntValue() / 100)))
                .listener((option, value) -> reference.intValue(value))
                .build();
    }

    private static Text title(String path) {
        return yaclText("title", path);
    }

    private static Text category(String path) {
        return yaclText("category", path);
    }

    private static Text tooltip(String path) {
        return yaclText("tooltip", path);
    }

    private static Text group(String path) {
        return yaclText("group", path);
    }

    private static Text description(String path) {
        return yaclText("description", path);
    }

    private static Text option(String path) {
        return yaclText("option", path);
    }

    private static Text yaclText(String type, String path) {
        return Text.translatable("%s.yacl.%s.%s", BetterTrims.MOD_ID, type, path);
    }
}
