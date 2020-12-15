package com.pixelcat.spring.boot.autoconfigure;

import com.pixelcat.core.zk.listener.DefaultNodeListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "org.springframework.boot.context.properties.bind.Binder")
@EnableConfigurationProperties({PixelCatProperties.class})
public class PixelCatAutoConfiguration {
}
