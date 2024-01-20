package com.bawnorton.bettertrims.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntOption {
    String group() default "default";

    int value();

    int min() default 0;

    int max() default 100;
}
