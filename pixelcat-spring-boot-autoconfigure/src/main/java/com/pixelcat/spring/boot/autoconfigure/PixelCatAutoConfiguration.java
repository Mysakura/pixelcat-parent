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
@ConditionalOnClass(name = "org.springframework.boot.context.properties.bind.Binder")
@EnableConfigurationProperties({PixelCatProperties.class})
public class PixelCatAutoConfiguration {
}
