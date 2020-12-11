package com.pixelcat.core.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class PixelCatBeanUtils {


    public static void registerBeanDefinition(String beanName, Class<?> clazz, BeanDefinitionRegistry registry, Object... constructorArgs){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(clazz);
        for (Object constructorArg : constructorArgs) {
            beanDefinitionBuilder.addConstructorArgValue(constructorArg);
        }
        // Bean作为基础设施
        beanDefinitionBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        // 注册
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

}
