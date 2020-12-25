package com.pixelcat.core.zk.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pixelcat.core.config.PixelCatPropertiesConstant;
import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.http.OkHttpUtil;
import com.pixelcat.core.zk.AbstractNodeListener;
import com.pixelcat.core.zk.handle.ConfigHandler;
import com.pixelcat.core.zk.handle.DefaultConfigHandler;
import com.pixelcat.core.zk.subject.ConfigChangeListener;
import com.pixelcat.core.zk.subject.ConfigSubject;
import com.pixelcat.core.zk.subject.DefaultConfigSubject;
import com.pixelcat.core.zk.subject.event.BaseConfigEvent;
import com.pixelcat.core.zk.subject.event.ConfigCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ZK节点监听，事件由configSubject来传播，接收者为 {@link ConfigChangeListener}
 */
@Slf4j
public class DefaultNodeListener extends AbstractNodeListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    public static final String BEAN_NAME = "defaultNodeListener";

    private ConfigSubject configSubject;

    private ConfigurableApplicationContext applicationContext;

    private final ConcurrentLinkedQueue<BaseConfigEvent> deferredEvents = new ConcurrentLinkedQueue<>();

    private String centerUrl;
    private String projectId;
    private String envId;

    public DefaultNodeListener() {
    }

    @PostConstruct
    public void init(){
        this.configSubject = applicationContext.getBean(DefaultConfigSubject.BEAN_NAME, ConfigSubject.class);
        this.centerUrl = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.CENTER_URL);
        this.projectId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.PROJECT_ID);
        this.envId = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.ENV_ID);

        // zk监听
        try {
            ConfigHandler configHandler = applicationContext.getBean(DefaultConfigHandler.BEAN_NAME, ConfigHandler.class);
            configHandler.addWatcher(this, "/" + projectId + "/" + envId);
        } catch (Exception e) {
            throw new PixelCatException("初始化zk节点监听失败！" + e.getMessage(), e);
        }
    }

    @Override
    protected void addEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()) {
            log.debug("新增节点：{} + {}", path, data);
        }
        doPublish(path);
    }

    @Override
    protected void updateEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("更新节点：{} + {}", path, data);
        }
        doPublish(path);
    }

    @Override
    protected void removeEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("删除节点：{} + {}", path, data);
        }
        doPublish(path);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * 这里应该有问题，要考虑无namespace的path
     * @param path
     * @return
     */
    private ParsedPath parsePath(String path){
        String[] split = path.split("/");
        return new ParsedPath(split[2], split[3], split[4]);
    }

    /**
     * 容器刷新之后，统一处理
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {


        // 1. 初始化客户端监听
        Collection<ConfigChangeListener> values = applicationContext.getBeansOfType(ConfigChangeListener.class).values();
        // 添加到subject
        values.forEach(configSubject::addConfigChangeListener);

        // 2. 发布积攒的事件
        Iterator<BaseConfigEvent> iterator = deferredEvents.iterator();
        while (iterator.hasNext()) {
            BaseConfigEvent next = iterator.next();
            configSubject.publishEvent(next);
            iterator.remove();
        }
    }

    private static class ParsedPath{
        private String projectId;
        private String envId;
        private String namespace;

        public ParsedPath(String projectId, String envId, String namespace) {
            this.projectId = projectId;
            this.envId = envId;
            this.namespace = namespace;
        }
    }

    /**
     * 节点改变，发布事件
     * @param path
     */
    private void doPublish(String path){
        // 1. 解析path
        ParsedPath parsedPath = parsePath(path);
        if (parsedPath.projectId.equals(projectId) && parsedPath.envId.equals(envId) ) {

            // 2. 请求http，获取配置
            String bodyJson = String.format("{\"projectId\": \"%s\", \"envId\": \"%s\", \"name\": \"%s\"}", projectId, envId, parsedPath.namespace);
            OkHttpUtil.getInstance().post(centerUrl + "/namespace/singleConfig", bodyJson, json -> {
                log.info("获取配置：{}", json);
                JSONObject jsonObject = JSON.parseObject(json);
                String dataStr = jsonObject.getString("data");
                Map<String, Object> dataMap = JSON.parseObject(dataStr);
                if (dataMap == null) {
                    dataMap = new HashMap<>();
                }
                // 3. 发布事件
                ConfigCreateEvent event = new ConfigCreateEvent(parsedPath.projectId, parsedPath.envId, parsedPath.namespace, dataMap);
                if (applicationContext.isRunning()){
                    configSubject.publishEvent(event);
                }else{
                    deferredEvents.add(event);
                }
            });
        }
    }
}
