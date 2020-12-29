package com.pixelcat.core.election;

import com.pixelcat.core.exception.PixelCatException;
import com.pixelcat.core.zk.handle.ConfigNodeHandler;
import com.pixelcat.core.zk.handle.DefaultConfigNodeHandler;
import com.pixelcat.core.zk.listener.AbstractZkNodeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 集群选举
 * 1. 利用选举，客户端可以不指定配置中心URL，因为这个信息会存储在node节点。好处在于用户不需要知道谁是Master，但是选举可能出现的情况要考虑周全
 * 2. 利用nginx负载均衡，客户端需要指定配置中心URL，好处在于省去了选举的逻辑，但是也增加了运维成本
 */
@Slf4j
public class ElectionMaster extends AbstractZkNodeHandler implements ApplicationContextAware {
    public static final String BEAN_NAME = "electionMaster";

    public static final String MASTER_PATH = "/master_of_center";
    private String port;
    private ConfigNodeHandler configNodeHandler;
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        this.configNodeHandler = applicationContext.getBean(DefaultConfigNodeHandler.BEAN_NAME, ConfigNodeHandler.class);
        this.port = applicationContext.getEnvironment().getProperty("server.port");
        // 初始化的时候，先来一波竞选
        preemption(null);
        // 然后添加监听
        try {
            configNodeHandler.addWatcher(this, MASTER_PATH);
        } catch (Exception e) {
            throw new PixelCatException("Master节点监听失败！" + e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void addEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {

    }

    @Override
    protected void updateEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {

    }

    @Override
    protected void removeEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String data = new String(treeCacheEvent.getData().getData());
        // 主节点下线，剩余节点竞选
        preemption(data);
    }

    /**
     * 竞选：自己成功创建临时节点，自己就是Master
     */
    private void preemption(String data) {
        String value = null;
        try {
            InetAddress host = InetAddress.getLocalHost();
            String ip = host.getHostAddress();
            value = "http://" + ip + ":" + port;
            if (data != null){
                // Master自己下线，不需要执行后面内容
                if (value.equals(data)){
                    log.error("Master【{}】下线！", value);
                    return;
                }
            }
            // 尝试把自己的地址写入path，这个地址作为客户端请求的配置中心地址
            configNodeHandler.createEphemeralPath(MASTER_PATH, value);
        } catch (UnknownHostException e) {
            log.error("竞选前期出现错误！", e);
        } catch (PixelCatException e) {
            log.info("节点【{}】竞选失败！", value);
        }
    }

}
