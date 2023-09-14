package com.bawnorton.bettertrims.config.option.annotation;

import com.bawnorton.bettertrims.config.option.OptionType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FloatOption {
    OptionType type() default OptionType.VANILLA;
    float value();
    float min() default 0f;
    float max() default 100f;
}
