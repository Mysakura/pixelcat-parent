package com.pixelcat.core.zk.handle;

import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * 操作配置
 */
public interface ConfigHandler {

    void addWatcher(TreeCacheListener listener) throws Exception;

    void createEphemeralPath(String path, String value);

    void deletePath(String path);

    String getPathValue(String path);

    void setPathValue(String path, String value);
}
