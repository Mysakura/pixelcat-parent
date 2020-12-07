package com.pixelcat.spring.boot.autoconfigure.listener;

import com.pixelcat.core.zk.BaseNodeListener;
import com.pixelcat.core.zk.subject.ConfigSubject;
import com.pixelcat.core.zk.subject.event.ConfigCreateEvent;
import com.pixelcat.core.zk.subject.event.ConfigDeleteEvent;
import com.pixelcat.core.zk.subject.event.ConfigUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class DefaultNodeListener extends BaseNodeListener {

    private ConfigSubject configSubject;

    @Autowired
    public DefaultNodeListener(ConfigSubject configSubject) {
        this.configSubject = configSubject;
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
}
