package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.config.bind.PixelCatConfigurationPropertiesBinder;
import com.pixelcat.core.config.processor.PixelCatConfigAnnotationBindingPostProcessor;
import com.pixelcat.core.zk.ZkServer;
import com.pixelcat.core.zk.handle.DefaultConfigNodeHandler;
import com.pixelcat.core.zk.listener.DefaultZkNodeHandler;
import com.pixelcat.core.zk.subject.DefaultConfigSubject;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static com.pixelcat.core.config.PixelCatBeanUtils.registerBeanDefinition;

public class PixelCatClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 属性绑定
        registerBeanDefinition(PixelCatConfigurationPropertiesBinder.BEAN_NAME, PixelCatConfigurationPropertiesBinder.class, registry);
        // 配置中心配置：后置处理器
        registerBeanDefinition(PixelCatConfigAnnotationBindingPostProcessor.BEAN_NAME, PixelCatConfigAnnotationBindingPostProcessor.class, registry);

        registerBeanDefinition(ZkServer.BEAN_NAME, ZkServer.class, registry);
        registerBeanDefinition(DefaultConfigNodeHandler.BEAN_NAME, DefaultConfigNodeHandler.class, registry);
        registerBeanDefinition(DefaultConfigSubject.BEAN_NAME, DefaultConfigSubject.class, registry);
        registerBeanDefinition(DefaultZkNodeHandler.BEAN_NAME, DefaultZkNodeHandler.class, registry);
    }
}
