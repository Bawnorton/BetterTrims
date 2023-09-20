package com.bawnorton.bettertrims.config.option.annotation;

import com.bawnorton.bettertrims.config.option.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BooleanOption {
    OptionType type() default OptionType.VANILLA;

    boolean value();
}
