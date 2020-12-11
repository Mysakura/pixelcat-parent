package com.pixelcat.core.config.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import com.pixelcat.core.config.bind.PixelCatConfigurationPropertiesBinder;

public class PixelCatConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    public static final String BEAN_NAME = "pixelCatConfigurationPropertiesBindingPostProcessor";

    private ConfigurableApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        PixelCatConfigurationPropertiesBinder binder = applicationContext.getBean(
                PixelCatConfigurationPropertiesBinder.BEAN_NAME,
                PixelCatConfigurationPropertiesBinder.class);
        binder.bind();
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
