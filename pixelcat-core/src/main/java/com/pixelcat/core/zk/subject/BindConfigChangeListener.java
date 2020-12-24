package com.pixelcat.core.zk.subject;


import com.pixelcat.core.config.PixelCatValue;
import com.pixelcat.core.zk.subject.event.BaseConfigEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

/**
 * 监听配置变动，更新配置
 */
@Slf4j
public class BindConfigChangeListener implements ConfigChangeListener{


    private String projectId;
    private String envId;
    private String namespace;

    private Object bean;
    private String beanName;

    public BindConfigChangeListener(String projectId, String envId, String namespace, Object bean, String beanName) {
        this.projectId = projectId;
        this.envId = envId;
        this.namespace = namespace;
        this.bean = bean;
        this.beanName = beanName;
    }

    @Override
    public void action(BaseConfigEvent event) {
        // 事件到来时，只有和当前监听的namespace一致，才处理
        if (namespace.equals(event.namespace())){
            ReflectionUtils.doWithFields(bean.getClass(), field -> {
                PixelCatValue annotation = getAnnotation(field, PixelCatValue.class);
                doWithAnnotation(beanName, bean, annotation, field.getModifiers(),
                        null, field, event);
            });
        }
    }

    private void doWithAnnotation(String beanName, Object bean, PixelCatValue annotation,
                                  int modifiers, Method method, Field field, BaseConfigEvent event) throws IllegalAccessException {
        if (log.isDebugEnabled()){
            log.debug("处理字段：{}，{}，{}，{}，{}", beanName, bean, method, field, event);
        }
        if (annotation != null) {
            if (Modifier.isStatic(modifiers)) {
                return;
            }
            Map<String, Object> dataMap = event.dataMap();
            String placeholder = annotation.key();
            Object value = dataMap.get(placeholder);

            field.setAccessible(true);
            field.set(bean, value);
        }
    }
}
