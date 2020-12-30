package com.pixelcat.core.zk.handle;

import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * 操作配置
 */
public interface ConfigNodeHandler {

    void addTreeWatcher(TreeCacheListener listener) throws Exception;

    void addTreeWatcher(TreeCacheListener listener, String path) throws Exception;

    void removeTreeWatcher(String path);

    void createEphemeralPath(String path, String value);

    void createPersistentPath(String path, String value);

    void deletePath(String path);

    String getPathValue(String path);

    void setPathValue(String path, String value);

    String listPath();

    boolean isExist(String path);

    boolean isClose();
}
