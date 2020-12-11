package com.pixelcat.core.config.bind;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class PixelCatConfigurationPropertiesBinder {
    public static final String BEAN_NAME = "pixelCatConfigurationPropertiesBinder";

    private final ConfigurableApplicationContext applicationContext;

    private final Environment environment;

    public PixelCatConfigurationPropertiesBinder(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    public void bind(){

    }
}
