package com.bawnorton.bettertrims.config.option;

import com.bawnorton.bettertrims.config.Config;
import com.bawnorton.bettertrims.config.option.annotation.BooleanOption;
import com.bawnorton.bettertrims.config.option.annotation.FloatOption;
import com.bawnorton.bettertrims.config.option.annotation.IntOption;
import com.bawnorton.bettertrims.config.option.annotation.NestedOption;
import com.bawnorton.bettertrims.util.Reflection;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

public final class ConfigOptionReference {
    private final Field field;
    private @NotNull Object value;

    public ConfigOptionReference(Field field) {
        this.field = field;
        this.value = Reflection.accessField(field, Config.getInstance());
        if(value == null) throw new IllegalStateException("Value for " + field.getName() + " field is null.");
    }

    public static ConfigOptionReference of(Field field) {
        return new ConfigOptionReference(field);
    }

    public String getFormattedName() {
        StringBuilder builder = new StringBuilder();
        String name = field.getName();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('.');
                builder.append(Character.toLowerCase(c));
            } else builder.append(c);
        }
        return builder.toString();
    }

    public Type getType() {
        if (value instanceof Boolean) return Type.BOOLEAN;
        if (value instanceof Integer) return Type.INTEGER;
        if (value instanceof Float) return Type.FLOAT;
        if (value instanceof NestedConfigOption) return Type.NESTED;
        throw new IllegalArgumentException("Unknown optionType: " + value.getClass().getName());
    }

    public boolean notOf(OptionType type) {
        return type != switch (getType()) {
            case BOOLEAN -> field.getAnnotation(BooleanOption.class).type();
            case INTEGER -> field.getAnnotation(IntOption.class).type();
            case FLOAT -> field.getAnnotation(FloatOption.class).type();
            case NESTED -> field.getAnnotation(NestedOption.class).type();
        };
    }

    private void validateType(Class<?> clazz) {
        if (!clazz.isAssignableFrom(value.getClass())) throw new IllegalArgumentException("Expected type " + clazz.getName() + ", got " + value.getClass().getName());
    }

    private <T> void setConfigValue(T value) {
        if (value == null) throw new IllegalArgumentException("Value cannot be null.");
        validateType(value.getClass());
        Reflection.setField(field, Config.getInstance(), value);
        this.value = value;
    }

    public void booleanValue(Boolean value) {
        setConfigValue(value);
    }

    public void intValue(Integer value) {
        setConfigValue(value);
    }

    public void floatValue(Float value) {
        setConfigValue(value);
    }

    private <T> T getValueAsType(Class<T> clazz) {
        validateType(clazz);
        return clazz.cast(value);
    }

    public Boolean booleanValue() {
        return getValueAsType(Boolean.class);
    }

    public Integer intValue() {
        return getValueAsType(Integer.class);
    }

    public Float floatValue() {
        return getValueAsType(Float.class);
    }

    public NestedConfigOption nestedValue() {
        return getValueAsType(NestedConfigOption.class);
    }

    public Number minValue() {
        return switch (getType()) {
            case FLOAT -> field.getAnnotation(FloatOption.class).min();
            case INTEGER -> field.getAnnotation(IntOption.class).min();
            case BOOLEAN, NESTED -> throw new IllegalArgumentException("Cannot get min value for type " + getType());
        };
    }

    public Number maxValue() {
        return switch (getType()) {
            case FLOAT -> field.getAnnotation(FloatOption.class).max();
            case INTEGER -> field.getAnnotation(IntOption.class).max();
            case BOOLEAN, NESTED -> throw new IllegalArgumentException("Cannot get max value for type " + getType());
        };
    }

    public Float minFloatValue() {
        return (Float) minValue();
    }

    public Float maxFloatValue() {
        return (Float) maxValue();
    }

    public Integer minIntValue() {
        return (Integer) minValue();
    }

    public Integer maxIntValue() {
        return (Integer) maxValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConfigOptionReference) obj;
        return Objects.equals(this.field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }

    @Override
    public String toString() {
        return "ConfigOptionReference[" +
                "field=" + field + ']';
    }

    public enum Type {
        BOOLEAN,
        INTEGER,
        FLOAT,
        NESTED
    }
}