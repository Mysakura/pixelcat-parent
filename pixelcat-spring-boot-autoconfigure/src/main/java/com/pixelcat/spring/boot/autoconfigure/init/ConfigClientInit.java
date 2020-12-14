package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.config.PixelCatPropertiesConstant;
import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.http.OkHttpUtil;
import com.pixelcat.core.zk.handle.ConfigHandler;
import com.pixelcat.core.zk.handle.DefaultConfigHandler;
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

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * 客户端初始化配置中心
 * 访问后台提供的http地址，初始化本地分布式配置
 */
public class ConfigClientInit implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    public static final String BEAN_NAME = "configClientInit";
    private ApplicationContext applicationContext;
    private ConfigHandler configHandler;
    private DefaultNodeListener defaultNodeListener;

    public ConfigClientInit() {
    }

    @PostConstruct
    public void init(){
        this.configHandler = applicationContext.getBean(DefaultConfigHandler.BEAN_NAME, ConfigHandler.class);
        this.defaultNodeListener = applicationContext.getBean(DefaultNodeListener.BEAN_NAME, DefaultNodeListener.class);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String centerUrl = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.CENTER_URL);
        String projectId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.PROJECT_ID);
        String envId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.ENV_ID);
        String bodyJson = String.format("{\"projectId\": %s, \"envId\": %s}", projectId, envId);
        // 1. 初始化配置:请求后台地址
        // 这个或许可以省略，SpringEvent也可以省略，直接用zk监听。或许可以用SpringEvent包装一下
//        OkHttpUtil.getInstance().post(centerUrl + "/namespace/initConfig", bodyJson, json -> {
//            System.out.println(json);
//        });

        // 2. zk监听
        try {
            configHandler.addWatcher(defaultNodeListener, "/" + projectId + "/" + envId);
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
