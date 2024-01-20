package com.bawnorton.bettertrims.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Groups {
    String[] value();
}
