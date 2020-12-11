package com.pixelcat.spring.boot.autoconfigure.init;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import(ConfigCenterInit.class)
@Import(PixelCatCenterBeanDefinitionRegistrar.class)
public @interface EnablePixelCatCenter {
}
