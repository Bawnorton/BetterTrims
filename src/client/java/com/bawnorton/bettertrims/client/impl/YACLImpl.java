package com.bawnorton.bettertrims.client.impl;

import com.bawnorton.bettertrims.client.config.ClientConfigManager;
import com.bawnorton.bettertrims.client.networking.ClientNetworking;
import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.config.annotation.Groups;
import com.bawnorton.bettertrims.config.option.ConfigOptionReference;
import com.bawnorton.bettertrims.config.option.ParentedConfigOptionReference;
import com.bawnorton.bettertrims.reflection.Reflection;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class YACLImpl {
    private static boolean inScreen = false;
    private static Screen screenParent = null;

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                                  .title(yaclText("title", "main"))
                                  .categories(generateCategories())
                                  .save(() -> {
                                      boolean inWorld = MinecraftClient.getInstance().world != null;
                                      if (inWorld && ClientNetworking.isConnectedToDedicated()) {
                                          ClientNetworking.trySendConfigToServer();
                                          return;
                                      }

                                      inScreen = false;
                                      ConfigManager.saveLocalConfig();
                                  })
                                  .screenInit(yaclScreen -> {
                                      inScreen = true;
                                      screenParent = parent;
                                  })
                                  .build()
                                  .generateScreen(parent);
    }

    public static void refreshScreen() {
        if (!inScreen) return;

        MinecraftClient.getInstance().setScreen(getScreen(screenParent));
    }

    private static Collection<ConfigCategory> generateCategories() {
        Collection<ConfigCategory> categories = new ArrayList<>();
        Groups groups = Reflection.getAnnotation(ClientConfigManager.getConfig(), Groups.class);
        List<String> groupNames = new ArrayList<>(Arrays.asList(groups.value()));
        Reflection.forEachAnnotatedField(ClientConfigManager.getConfig(), field -> ConfigOptionReference.readGroup(field).ifPresent(group -> {
            if(!groupNames.contains(group)) {
                throw new IllegalStateException("Group \"" + group + "\" does not exist. It is referenced by field \"" + field.getName() + "\".");
            }
        }));
        for (String group : groupNames) {
            categories.add(generateCategoryForGroup(group));
        }
        return categories;
    }

    private static ConfigCategory generateCategoryForGroup(String group) {
        return ConfigCategory.createBuilder()
                             .name(category(group))
                             .tooltip(tooltip(group))
                             .group(OptionGroup.createBuilder()
                                               .name(group(group))
                                               .description(OptionDescription.of(description(group)))
                                               .options(generateOptionsForGroup(group))
                                               .build())
                             .build();
    }

    private static Collection<? extends Option<?>> generateOptionsForGroup(String group) {
        Collection<Option<?>> options = new HashSet<>();
        Reflection.forEachAnnotatedField(ClientConfigManager.getConfig(), field -> {
            ConfigOptionReference reference = ConfigOptionReference.of(ClientConfigManager.getConfig(), field);
            if (!reference.isOf(group)) return;

            if (reference.isNested()) {
                options.addAll(createNestedOptions(reference, group));
            } else {
                options.add(createOption(reference));
            }
        });
        return options.stream().sorted(Comparator.comparing(option -> option.name().getString())).toList();
    }

    private static Collection<? extends Option<?>> createNestedOptions(ConfigOptionReference reference, String type) {
        if (!reference.isNested())
            throw new IllegalArgumentException("Reference \"%s\" is not nested".formatted(reference.getFormattedName()));

        Collection<Option<?>> options = new ArrayList<>();
        Object instance = reference.nestedValue();
        Reflection.forEachAnnotatedField(instance, nestedField -> {
            ParentedConfigOptionReference parentedReference = ParentedConfigOptionReference.of(reference, instance, nestedField);
            if (!parentedReference.isOf(type)) return;

            if (parentedReference.isNested()) {
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
            case NESTED ->
                    throw new IllegalArgumentException("Attempted to of non-nested option for nested reference \"%s\"".formatted(reference.getFormattedName()));
        };
    }

    private static Option<Float> floatOption(ConfigOptionReference reference) {
        return Option.<Float>createBuilder()
                     .name(option(reference.getFormattedName()))
                     .description(imagedDescription(reference))
                     .binding(Binding.generic(reference.floatValue(), reference::floatValue, reference::floatValue))
                     .controller(option -> FloatSliderControllerBuilder.create(option)
                                                                       .formatValue(value -> Text.literal(String.format("%,.2f", value)
                                                                                                                .replaceAll("[\u00a0\u202F]", " ")))
                                                                       .range(reference.minFloatValue(), reference.maxFloatValue())
                                                                       .step(reference.maxFloatValue() / 100f))
                     .build();
    }

    private static Option<Boolean> booleanOption(ConfigOptionReference reference) {
        return Option.<Boolean>createBuilder()
                     .name(option(reference.getFormattedName()))
                     .description(imagedDescription(reference))
                     .binding(Binding.generic(reference.booleanValue(), reference::booleanValue, reference::booleanValue))
                     .controller(TickBoxControllerBuilder::create)
                     .build();
    }

    private static Option<Integer> integerOption(ConfigOptionReference reference) {
        return Option.<Integer>createBuilder()
                     .name(option(reference.getFormattedName()))
                     .description(imagedDescription(reference))
                     .binding(Binding.generic(reference.intValue(), reference::intValue, reference::intValue))
                     .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                                         .range(reference.minIntValue(), reference.maxIntValue())
                                                                         .step(Math.max(1, reference.maxIntValue() / 100)))
                     .build();
    }

    private static OptionDescription imagedDescription(ConfigOptionReference reference) {
        Identifier textureLocation = reference.findTexture();
        if (textureLocation == null || MinecraftClient.getInstance().getResourceManager().getResource(textureLocation).isEmpty()) {
            return OptionDescription.of(description(reference.getFormattedName()));
        }

        return OptionDescription.createBuilder()
                                .image(textureLocation, 16, 16)
                                .text(description(reference.getFormattedName()))
                                .build();
    }

    private static MutableText category(String key) {
        return yaclText("category", key);
    }

    private static MutableText tooltip(String key) {
        return yaclText("tooltip", key);
    }

    private static MutableText group(String key) {
        return yaclText("group", key);
    }

    private static MutableText description(String key) {
        return yaclText("description", key);
    }

    private static MutableText option(String key) {
        return yaclText("option", key);
    }

    private static MutableText yaclText(String category, String key) {
        return Text.translatable("bettertrims.yacl.%s.%s".formatted(category, key));
    }
}
