package com.pixelcat.core.config;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PixelCatConfig {

    /**
     * namespace
     * @return
     */
    String namespace() default "";
}
