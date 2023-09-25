package com.bawnorton.bettertrims.config.annotation;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TextureLocation {
    String value() default "none";

    ArmorTrimEffects effectLookup() default ArmorTrimEffects.NONE;
}
