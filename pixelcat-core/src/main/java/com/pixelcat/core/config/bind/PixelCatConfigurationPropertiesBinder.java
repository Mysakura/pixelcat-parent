package com.pixelcat.core.config.bind;

import com.pixelcat.core.config.PixelCatPropertiesConstant;
import com.pixelcat.core.zk.subject.BindConfigChangeListener;
import com.pixelcat.core.zk.subject.ConfigSubject;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PixelCatConfigurationPropertiesBinder {
    public static final String BEAN_NAME = "pixelCatConfigurationPropertiesBinder";

    private final ConfigurableApplicationContext applicationContext;

    private final Environment environment;

    private static Map<String,Class<?>> pixelCatConfigClass = new ConcurrentHashMap<>();

    public PixelCatConfigurationPropertiesBinder(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }


    public void bind(Object bean, String beanName, String namespace){
        Class<?> clazz = bean.getClass();
        pixelCatConfigClass.put(namespace, clazz);

        // 配置和namespace绑定
        String projectId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.PROJECT_ID);
        String envId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.ENV_ID);
        ConfigSubject subject = applicationContext.getBean(ConfigSubject.class);
        subject.addConfigChangeListener(new BindConfigChangeListener(projectId, envId, namespace, bean, beanName));
    }

}
