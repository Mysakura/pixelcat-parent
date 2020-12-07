package com.pixelcat.spring.boot.autoconfigure;

import com.pixelcat.core.db.DefaultExecutorFactory;
import com.pixelcat.core.db.executor.ExecutorFactory;
import com.pixelcat.core.zk.ZkServer;
import com.pixelcat.core.zk.handle.ConfigHandler;
import com.pixelcat.core.zk.handle.DefaultConfigHandler;
import com.pixelcat.core.zk.subject.ConfigSubject;
import com.pixelcat.core.zk.subject.DefaultConfigSubject;
import com.pixelcat.spring.boot.autoconfigure.listener.DefaultNodeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
@EnableConfigurationProperties({PixelCatProperties.class})
public class PixelCatAutoConfiguration {

    @Autowired
    private PixelCatProperties properties;

    @Bean
    public ZkServer zkServer(){
        return new ZkServer(properties.getZkUrl(), properties.getZkSleepTimeMs(), properties.getZkMaxRetries());
    }

    @Bean
    public ConfigHandler configHandler(){
        return new DefaultConfigHandler(zkServer(), properties.getZkRootPath());
    }

    @Bean
    public ConfigSubject configSubject(){
        return new DefaultConfigSubject();
    }

    @Bean
    public DefaultNodeListener defaultNodeListener(ConfigSubject configSubject){
        return new DefaultNodeListener(configSubject);
    }

    @Bean
    public ExecutorFactory executorFactory(){
        return new DefaultExecutorFactory(properties.getDriver(), properties.getUrl(), properties.getUsername(), properties.getPassword());
    }
}
