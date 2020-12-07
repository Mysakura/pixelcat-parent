package com.pixelcat.core.zk.handle;

import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.zk.ZkServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.util.StringUtils;

@Slf4j
public class DefaultConfigHandler implements ConfigHandler {

    private ZkServer zkServer;
    private String rootPath;

    public DefaultConfigHandler(ZkServer zkServer, String rootPath) {
        this.zkServer = zkServer;
        this.rootPath = rootPath;
    }

    private String prePath(String path){
        if (StringUtils.isEmpty(path)){
            throw new PixelCatException("传入的zk节点path不能为空！");
        }
        if (!path.startsWith("/")){
            return rootPath + "/" + path;
        }
        return rootPath + path;
    }

    @Override
    public void addWatcher(TreeCacheListener listener) throws Exception {
        zkServer.addWatcher(listener, rootPath);
    }

    @Override
    public void createEphemeralPath(String path, String value) {
        try {
            zkServer.createEphemeralPath(prePath(path), value);
        } catch (Exception e) {
            log.error("创建临时节点失败！", e);
            throw new PixelCatException("创建临时节点失败！Cause：" + e.getMessage());
        }
    }

    @Override
    public void deletePath(String path) {
        try {
            zkServer.deletePath(prePath(path));
        } catch (Exception e) {
            log.error("删除节点失败！", e);
            throw new PixelCatException("删除节点失败！Cause：" + e.getMessage());
        }
    }

    @Override
    public String getPathValue(String path) {
        try {
            return zkServer.getPathValue(prePath(path));
        } catch (Exception e) {
            log.error("获取节点value失败！", e);
            throw new PixelCatException("获取节点value失败！Cause：" + e.getMessage());
        }

    }

    @Override
    public void setPathValue(String path, String value) {
        try {
            zkServer.setPathValue(prePath(path), value);
        } catch (Exception e) {
            log.error("设置节点value失败！", e);
            throw new PixelCatException("设置节点value失败！Cause：" + e.getMessage());
        }
    }
}
