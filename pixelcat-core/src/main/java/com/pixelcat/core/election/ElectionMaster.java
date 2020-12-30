package com.pixelcat.core.election;

import com.pixelcat.core.db.DefaultExecutorFactory;
import com.pixelcat.core.db.executor.Executor;
import com.pixelcat.core.db.executor.ExecutorFactory;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 集群选举
 * 客户端可以不指定配置中心URL，因为这个信息会存储在node节点。好处在于用户不需要知道谁是Master，但是选举可能出现的情况要考虑周全
 * 1. 选举成功的作为Master，Master对外提供服务
 * 2. 比如三个管理台ABC，任何一个都可以对外提供前台页面的操作。但是对于客户端的请求，只有Master来处理。
 */
@Slf4j
public class ElectionMaster extends AbstractZkNodeHandler implements ApplicationContextAware {
    public static final String BEAN_NAME = "electionMaster";

    public static final String MASTER_PATH = "/master_of_center";
    private String port;
    private ConfigNodeHandler configNodeHandler;
    private ApplicationContext applicationContext;
    private ExecutorFactory executorFactory;

    @PostConstruct
    public void init(){
        this.configNodeHandler = applicationContext.getBean(DefaultConfigNodeHandler.BEAN_NAME, ConfigNodeHandler.class);
        this.executorFactory = applicationContext.getBean(DefaultExecutorFactory.BEAN_NAME, ExecutorFactory.class);
        this.port = applicationContext.getEnvironment().getProperty("server.port");
        // 初始化的时候，先来一波竞选
        boolean preemption = preemption(null);
        if (preemption){
            // 竞选成功，初始化自己的节点
            initZkNode(readFromDb());
        }
        // 然后添加监听
        try {
            configNodeHandler.addTreeWatcher(this, MASTER_PATH);
        } catch (Exception e) {
            throw new PixelCatException("Master节点监听失败！" + e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void removeEvent(CuratorFramework curatorFramework, TreeCacheEvent cacheEvent) {
        String path = cacheEvent.getData().getPath();
        String data = new String(cacheEvent.getData().getData());
        if (log.isDebugEnabled()){
            log.debug("节点【{}】【{}】被移除！", path, data);
        }

        // 主节点下线，剩余节点竞选
        boolean preemption = preemption(data);
        if (preemption){
            // 竞选成功，初始化自己的节点
            initZkNode(readFromDb());
        }
    }

    /**
     * 竞选：自己成功创建临时节点，自己就是Master
     */
    private boolean preemption(String data) {
        String value = null;
        try {
            InetAddress host = InetAddress.getLocalHost();
            String ip = host.getHostAddress();
            value = "http://" + ip + ":" + port;
            if (data != null){
                // Master自己下线，不需要执行后面内容
                if (value.equals(data)){
                    log.error("Master【{}】下线！", value);
                    return false;
                }
            }
            // 尝试把自己的地址写入path，这个地址作为客户端请求的配置中心地址
            if (!configNodeHandler.isExist(MASTER_PATH)) {
                configNodeHandler.createEphemeralPath(MASTER_PATH, value);
                log.info("节点【{}】竞选成功！", value);
                return true;
            } else {
                log.info("Path【{}】已存在！节点【{}】竞选失败！", MASTER_PATH, value);
                return false;
            }
        } catch (UnknownHostException e) {
            log.error("竞选前期出现错误！", e);
            return false;
        } catch (PixelCatException e) {
            log.info("节点【{}】竞选失败！", value);
            return false;
        }
    }

    /**
     * 读取数据库
     * @return
     */
    private List<NameSpace> readFromDb(){
        Executor executor = executorFactory.newExecutor();
        NameSpace record = new NameSpace();
        record.setType(3);
        record.setDeleteFlag(1);
        List<NameSpace> list = executor.getList(NameSpace.class, record);
        executor.close();
        return list;
    }

    private void initZkNode(List<NameSpace> nameSpaces){
        if (CollectionUtils.isEmpty(nameSpaces)){
            log.warn("没有需要初始化的节点，请检查数据库！");
        }else {
            nameSpaces.forEach(e -> {
                String node = e.getProjectId() + "/" + e.getEnvId() + "/" + e.getName();
                configNodeHandler.createEphemeralPath(node, "INIT");
            });
            // 打印节点
            log.info("初始化配置节点：\n*******************************\n{}\n*******************************", configNodeHandler.listPath());
        }
    }

}
