package com.bawnorton.bettertrims.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import java.awt.Color;
import java.util.function.UnaryOperator;

public interface Styler {
    static Style normal(Style style) {
        return style.withColor(ChatFormatting.GRAY);
    }

    static Style condition(Style style) {
        return style.withColor(Color.LIGHT_GRAY.getRGB());
    }

    static Style number(Style style) {
        return style.withColor(ChatFormatting.AQUA);
    }

    static Style name(Style style) {
        return style.withColor(ChatFormatting.LIGHT_PURPLE);
    }

    static Style trim(Style style) {
        return  style.withColor(ChatFormatting.GOLD);
    }

    static Style property(Style style) {
        return style.withColor(ChatFormatting.DARK_PURPLE);
    }

    static Style target(Style style) {
        return style.withColor(new Color(255, 128, 0).getRGB());
    }

    static Style value(Style style) {
        return style.withColor(ChatFormatting.GREEN);
    }

    static Style component(Style style) {
        return style.withColor(ChatFormatting.DARK_AQUA);
    }

    static Style positive(Style style) {
        return style.withColor(ChatFormatting.BLUE);
    }

    static Style negative(Style style) {
        return style.withColor(ChatFormatting.RED);
    }

    static UnaryOperator<Style> sentiment(boolean beneficial) {
        return beneficial ? Styler::positive : Styler::negative;
    }

    static MutableComponent normal(MutableComponent component) {
        return component.withStyle(Styler::normal);
    }

    static MutableComponent number(MutableComponent component) {
        return component.withStyle(Styler::number);
    }

    static MutableComponent condition(MutableComponent component) {
        return component.withStyle(Styler::condition);
    }

    static MutableComponent name(MutableComponent component) {
        return component.withStyle(Styler::name);
    }

    static MutableComponent trim(MutableComponent component) {
        return component.withStyle(Styler::trim);
    }

    static MutableComponent property(MutableComponent component) {
        return component.withStyle(Styler::property);
    }

    static MutableComponent target(MutableComponent component) {
        return component.withStyle(Styler::target);
    }

    static MutableComponent value(MutableComponent component) {
        return component.withStyle(Styler::value);
    }

    static MutableComponent component(MutableComponent component) {
        return component.withStyle(Styler::component);
    }

    static MutableComponent positive(MutableComponent component) {
        return component.withStyle(Styler::positive);
    }

    static MutableComponent negative(MutableComponent component) {
        return component.withStyle(Styler::negative);
    }

    static MutableComponent sentiment(MutableComponent component, boolean beneficial) {
        return component.withStyle(sentiment(beneficial));
    }

}
