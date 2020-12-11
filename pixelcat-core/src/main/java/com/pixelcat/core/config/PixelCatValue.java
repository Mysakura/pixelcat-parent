package com.pixelcat.core.config;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PixelCatValue {

    /**
     * key
     * @return
     */
    String key() default "";
}
