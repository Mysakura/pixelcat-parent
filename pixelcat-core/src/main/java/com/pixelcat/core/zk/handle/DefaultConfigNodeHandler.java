package com.pixelcat.core.zk.handle;

import com.pixelcat.core.config.PixelCatPropertiesConstant;
import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.zk.ZkServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Slf4j
public class DefaultConfigNodeHandler implements ConfigNodeHandler, ApplicationContextAware, DisposableBean {
    public static final String BEAN_NAME = "defaultConfigHandler";

    private ZkServer zkServer;
    private String rootPath;
    private ApplicationContext applicationContext;

    public DefaultConfigNodeHandler() {

    }

    @PostConstruct
    public void init(){
        this.zkServer = applicationContext.getBean(ZkServer.class);
        this.rootPath = applicationContext.getEnvironment().getProperty(PixelCatPropertiesConstant.ZK_ROOT_PATH);
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
    public void addTreeWatcher(TreeCacheListener listener) throws Exception {
        zkServer.addWatcher(listener, rootPath);
    }

    @Override
    public void addTreeWatcher(TreeCacheListener listener, String path) throws Exception {
        zkServer.addWatcher(listener, rootPath + path);
    }

    @Override
    public void removeTreeWatcher(String path) {
        zkServer.removeTreeCacheWatcher(rootPath + path);
    }

    @Override
    public void createEphemeralPath(String path, String value) {
        try {
            zkServer.createEphemeralPath(prePath(path), value);
        } catch (Exception e) {
            log.error("创建临时节点{}失败！", path, e);
            throw new PixelCatException("创建临时节点失败！Cause：" + e.getMessage());
        }
    }

    @Override
    public void deletePath(String path) {
        try {
            zkServer.deletePath(prePath(path));
        } catch (Exception e) {
            log.error("删除节点{}失败！", path, e);
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

    @Override
    public String listPath() {
        try {
            return zkServer.listPath(rootPath);
        } catch (Exception e) {
            log.error("获取配置节点失败！", e);
            throw new PixelCatException("获取配置节点失败！Cause：" + e.getMessage());
        }
    }

    @Override
    public boolean isExist(String path) {
        try {
            return zkServer.isExit(prePath(path));
        } catch (Exception e) {
            log.error("检测节点失败！", e);
            throw new PixelCatException("检测节点失败！Cause：" + e.getMessage());
        }

    }

    @Override
    public boolean isClose() {
        return zkServer.isClose();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        // 移除监听
        zkServer.removeTreeCacheWatchers();
        // 删除建立的节点
        if (zkServer.isExit(rootPath)) {
            zkServer.deletePath(rootPath);
        }
        // 关闭连接
        zkServer.close();
    }
}
