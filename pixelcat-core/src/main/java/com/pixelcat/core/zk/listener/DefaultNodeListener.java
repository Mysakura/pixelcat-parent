package com.pixelcat.core.zk.listener;

import com.pixelcat.core.zk.AbstractNodeListener;
import com.pixelcat.core.zk.subject.ConfigChangeListener;
import com.pixelcat.core.zk.subject.ConfigSubject;
import com.pixelcat.core.zk.subject.DefaultConfigSubject;
import com.pixelcat.core.zk.subject.event.ConfigCreateEvent;
import com.pixelcat.core.zk.subject.event.ConfigDeleteEvent;
import com.pixelcat.core.zk.subject.event.ConfigUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * ZK节点监听，事件由configSubject来传播，接收者为 {@link ConfigChangeListener}
 */
@Slf4j
public class DefaultNodeListener extends AbstractNodeListener implements ApplicationContextAware {
    public static final String BEAN_NAME = "defaultNodeListener";

    private ConfigSubject configSubject;

    private ApplicationContext applicationContext;

    public DefaultNodeListener() {
    }

    @PostConstruct
    public void init(){
        this.configSubject = applicationContext.getBean(DefaultConfigSubject.BEAN_NAME, ConfigSubject.class);
    }

    @Override
    protected void addEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("新增节点：{} + {}", path, data);
        }
        // TODO 请求http，更新本地配置
        configSubject.publishEvent(new ConfigCreateEvent(path, data));
    }

    @Override
    protected void updateEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("更新节点：{} + {}", path, data);
        }
        configSubject.publishEvent(new ConfigUpdateEvent(path, data));
    }

    @Override
    protected void removeEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String path = treeCacheEvent.getData().getPath();
        String data = new String(treeCacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("删除节点：{} + {}", path, data);
        }
        configSubject.publishEvent(new ConfigDeleteEvent(path, data));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
