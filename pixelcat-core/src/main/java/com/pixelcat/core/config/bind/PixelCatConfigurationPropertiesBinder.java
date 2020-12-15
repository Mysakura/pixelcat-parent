package com.pixelcat.core.config.bind;

import com.pixelcat.core.config.PixelCatValue;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

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

        ReflectionUtils.doWithFields(clazz, field -> {
            PixelCatValue annotation = getAnnotation(field, PixelCatValue.class);
            doWithAnnotation(beanName, bean, annotation, field.getModifiers(),
                    null, field);
        });
    }

    private void doWithAnnotation(String beanName, Object bean, PixelCatValue annotation,
                                  int modifiers, Method method, Field field) {
        if (annotation != null) {
            if (Modifier.isStatic(modifiers)) {
                return;
            }
            String placeholder = annotation.key();
            // TODO
        }
    }
}
