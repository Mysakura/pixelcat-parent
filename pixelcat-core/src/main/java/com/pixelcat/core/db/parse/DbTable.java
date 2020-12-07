package com.pixelcat.core.db.parse;

import java.lang.annotation.*;

/**
 * 表名注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbTable {
    String value() default "";
}
