package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.config.bind.PixelCatConfigurationPropertiesBinder;
import com.pixelcat.core.db.DefaultExecutorFactory;
import com.pixelcat.core.zk.ZkServer;
import com.pixelcat.core.zk.handle.DefaultConfigHandler;
import com.pixelcat.core.zk.subject.DefaultConfigSubject;
import com.pixelcat.core.zk.listener.DefaultNodeListener;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static com.pixelcat.core.config.PixelCatBeanUtils.registerBeanDefinition;

/**
 * 注册自身需要用到的组件
 */
public class PixelCatCenterBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 属性绑定
        registerBeanDefinition(PixelCatConfigurationPropertiesBinder.BEAN_NAME, PixelCatConfigurationPropertiesBinder.class, registry);

        registerBeanDefinition(ConfigCenterInit.BEAN_NAME, ConfigCenterInit.class, registry);
        registerBeanDefinition(ZkServer.BEAN_NAME, ZkServer.class, registry);
        registerBeanDefinition(DefaultConfigHandler.BEAN_NAME, DefaultConfigHandler.class, registry);
        registerBeanDefinition(DefaultConfigSubject.BEAN_NAME, DefaultConfigSubject.class, registry);
        registerBeanDefinition(DefaultNodeListener.BEAN_NAME, DefaultNodeListener.class, registry);
        registerBeanDefinition(DefaultExecutorFactory.BEAN_NAME, DefaultExecutorFactory.class, registry);
    }


}
