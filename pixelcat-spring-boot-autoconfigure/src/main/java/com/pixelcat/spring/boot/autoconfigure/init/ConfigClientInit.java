package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.http.OkHttpUtil;
import com.pixelcat.core.zk.handle.ConfigHandler;
import com.pixelcat.core.zk.subject.ConfigChangeListener;
import com.pixelcat.core.zk.subject.ConfigSubject;
import com.pixelcat.spring.boot.autoconfigure.PixelCatProperties;
import com.pixelcat.spring.boot.autoconfigure.listener.DefaultNodeListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collection;

/**
 * 客户端初始化配置中心
 * 访问后台提供的http地址，初始化本地分布式配置
 */
public class ConfigClientInit implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private PixelCatProperties properties;
    private ApplicationContext applicationContext;
    private ConfigHandler configHandler;
    private DefaultNodeListener defaultNodeListener;

    @Autowired
    public ConfigClientInit(PixelCatProperties properties, ConfigHandler configHandler, DefaultNodeListener defaultNodeListener) {
        this.properties = properties;
        this.configHandler = configHandler;
        this.defaultNodeListener = defaultNodeListener;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 1. 初始化配置:请求后台地址
        OkHttpUtil.getInstance().get(properties.getCenterUrl() + "/initConfig", json -> {

        });

        // 2. zk监听
        try {
            configHandler.addWatcher(defaultNodeListener);
        } catch (Exception e) {
            throw new PixelCatException("初始化zk根节点监听失败！" + e.getMessage(), e);
        }
        // 3. 初始化客户端监听
        ConfigSubject subject = applicationContext.getBean(ConfigSubject.class);
        Collection<ConfigChangeListener> values = applicationContext.getBeansOfType(ConfigChangeListener.class).values();
        values.forEach(subject::addConfigChangeListener);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
