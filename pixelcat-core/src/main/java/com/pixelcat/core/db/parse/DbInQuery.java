package com.pixelcat.core.db.parse;

import java.lang.annotation.*;

/**
 * 字段注解
 * in查询
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbInQuery {
}
