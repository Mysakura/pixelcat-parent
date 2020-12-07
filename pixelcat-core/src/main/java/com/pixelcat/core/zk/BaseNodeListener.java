package com.pixelcat.core.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

public abstract class BaseNodeListener implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

        TreeCacheEvent.Type eventType = treeCacheEvent.getType();
        switch (eventType){
            case INITIALIZED:
                break;
            case NODE_ADDED:    // 增加节点
                addEvent(curatorFramework, treeCacheEvent);
                break;
            case NODE_REMOVED:  // 删除节点
                removeEvent(curatorFramework, treeCacheEvent);
                break;
            case NODE_UPDATED:  // 修改节点
                updateEvent(curatorFramework, treeCacheEvent);
                break;
        }
    }

    protected abstract void addEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent);
    protected abstract void updateEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent);
    protected abstract void removeEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent);
}
