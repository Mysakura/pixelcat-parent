package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.spring.boot.autoconfigure.PixelCatProperties;
import com.pixelcat.core.config.PixelCatPropertiesConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * 将配置属性写入Spring上下文
 */
@Slf4j
public class PixelCatConfigEnvironmentProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PixelCatProperties pixelCatProperties = buildPixelCatProperties(environment);
        MapPropertySource properties = beanToProperties(environment, pixelCatProperties);
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        mutablePropertySources.addLast(properties);
    }


    private PixelCatProperties buildPixelCatProperties(ConfigurableEnvironment environment){
        PixelCatProperties pixelCatProperties = new PixelCatProperties();
        Binder binder = Binder.get(environment);
        ResolvableType type = ResolvableType.forClass(PixelCatProperties.class);
        Bindable<?> target = Bindable.of(type).withExistingValue(pixelCatProperties);
        binder.bind(PixelCatPropertiesConstant.PREFIX, target);
        return pixelCatProperties;
    }

    /**
     * org.springframework.core.env.PropertyResolver#resolvePlaceholders(java.lang.String) 获取全局配置如："${spring.application.name}"
     * @param environment
     * @param pixelCatProperties
     * @return
     */
    private MapPropertySource beanToProperties(ConfigurableEnvironment environment, PixelCatProperties pixelCatProperties){
        Map<String, Object> source = new HashMap<>();
        if (StringUtils.isNotBlank(pixelCatProperties.getProjectId())) {
            source.put(PixelCatPropertiesConstant.PROJECT_ID,
                    environment.resolvePlaceholders(pixelCatProperties.getProjectId()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getEnvId())) {
            source.put(PixelCatPropertiesConstant.ENV_ID,
                    environment.resolvePlaceholders(pixelCatProperties.getEnvId()));
        }

        if (StringUtils.isNotBlank(pixelCatProperties.getDriver())) {
            source.put(PixelCatPropertiesConstant.DRIVER,
                    environment.resolvePlaceholders(pixelCatProperties.getDriver()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getUrl())) {
            source.put(PixelCatPropertiesConstant.URL,
                    environment.resolvePlaceholders(pixelCatProperties.getUrl()));
        }

        if (StringUtils.isNotBlank(pixelCatProperties.getUsername())) {
            source.put(PixelCatPropertiesConstant.USERNAME,
                    environment.resolvePlaceholders(pixelCatProperties.getUsername()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getPassword())) {
            source.put(PixelCatPropertiesConstant.PASSWORD,
                    environment.resolvePlaceholders(pixelCatProperties.getPassword()));
        }

        if (StringUtils.isNotBlank(pixelCatProperties.getZkUrl())) {
            source.put(PixelCatPropertiesConstant.ZK_URL,
                    environment.resolvePlaceholders(pixelCatProperties.getZkUrl()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getZkRootPath())) {
            source.put(PixelCatPropertiesConstant.ZK_ROOT_PATH,
                    environment.resolvePlaceholders(pixelCatProperties.getZkRootPath()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getZkMaxRetries())) {
            source.put(PixelCatPropertiesConstant.ZK_MAX_RETRIES,
                    environment.resolvePlaceholders(pixelCatProperties.getZkMaxRetries()));
        }
        if (StringUtils.isNotBlank(pixelCatProperties.getZkSleepTimeMs())) {
            source.put(PixelCatPropertiesConstant.ZK_SLEEP_TIME_MS,
                    environment.resolvePlaceholders(pixelCatProperties.getZkSleepTimeMs()));
        }
        log.info("配置属性：{}", source);
        return new MapPropertySource("PixelCatPropertiesSource", source);
    }
}
