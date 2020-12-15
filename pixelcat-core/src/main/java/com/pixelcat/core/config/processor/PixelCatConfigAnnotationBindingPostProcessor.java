package com.pixelcat.core.config.processor;

import com.pixelcat.core.config.PixelCatConfig;
import com.pixelcat.core.exception.PixelCatException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import com.pixelcat.core.config.bind.PixelCatConfigurationPropertiesBinder;
import org.springframework.util.StringUtils;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * 配置类绑定
 */
public class PixelCatConfigAnnotationBindingPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    public static final String BEAN_NAME = "pixelCatConfigurationPropertiesBindingPostProcessor";

    private ConfigurableApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        PixelCatConfig annotation = findAnnotation(bean.getClass(), PixelCatConfig.class);
        if (annotation != null){
            String namespace = annotation.namespace();
            if (StringUtils.isEmpty(namespace)){
                throw new PixelCatException(String.format("Bean:%s 注解@PixelCatConfig未指定namespace", beanName));
            }
            PixelCatConfigurationPropertiesBinder binder = applicationContext.getBean(
                    PixelCatConfigurationPropertiesBinder.BEAN_NAME,
                    PixelCatConfigurationPropertiesBinder.class);
            binder.bind(bean, beanName, namespace);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
