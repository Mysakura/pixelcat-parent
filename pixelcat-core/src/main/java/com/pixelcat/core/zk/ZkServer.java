package com.pixelcat.core.zk;

import com.pixelcat.core.config.PixelCatPropertiesConstant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  zk存储结构： /pixelcat/[env]/[project]/[namespace]/XXX
 */
public class ZkServer implements EnvironmentAware {
    public static final String BEAN_NAME = "zkServer";

    private static final Map<String, PathChildrenCache> PATH_LISTENER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, NodeCache> NODE_LISTENER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, TreeCache> TREE_LISTENER_MAP = new ConcurrentHashMap<>();

    private static CuratorFramework client;

    private Environment environment;

    public ZkServer() {

    }

    @PostConstruct
    public void init(){
        String connectString = environment.getProperty(PixelCatPropertiesConstant.ZK_URL);
        String baseSleepTimeMs = environment.getProperty(PixelCatPropertiesConstant.ZK_SLEEP_TIME_MS);
        String maxRetries = environment.getProperty(PixelCatPropertiesConstant.ZK_MAX_RETRIES);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.valueOf(baseSleepTimeMs), Integer.valueOf(maxRetries));
        client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
    }

    /**
     *  创建临时节点
     *  ZooKeeper不支持临时节点下挂子节点，所以递归创建节点，只有最后的子节点是临时节点
     * @param path
     * @param value
     * @throws Exception
     */
    public void createEphemeralPath(String path, String value) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes());
    }

    public void createEphemeralPath(String path) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
    }

    /**
     * 创建顺序编号的临时节点
     * @param path
     * @param value
     * @throws Exception
     */
    public void createEphemeralSequentialPath(String path, String value) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, value.getBytes());
    }

    public void createEphemeralSequentialPath(String path) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
    }

    /**
     * 创建永久节点
     * @param path
     * @param value
     * @throws Exception
     */
    public void createPersistentPath(String path, String value) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, value.getBytes());
    }

    public void createPersistentPath(String path) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
    }

    /**
     * 创建顺序编号的永久节点
     * 即一个客户端创建/p节点,那么Zookeeper将把其节点命名为/p1,当另一个客户端也创建/p节点,那么Zookeeper将把该节点命名为/p2
     * @param path
     * @param value
     * @throws Exception
     */
    public void createPersistentSequentialPath(String path, String value) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, value.getBytes());
    }

    public void createPersistentSequentialPath(String path) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path);
    }


    /**
     * 删除节点
     * @param path
     * @throws Exception
     */
    public void deletePath(String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    /**
     * 列出所有节点
     * @throws Exception
     */
    public String listPath(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        printNode(path, -1, sb);
        return sb.toString();
    }

    private void printNode(String path, int n, StringBuilder sb) throws Exception {
        List<String> children = client.getChildren().forPath(path);
        if (!CollectionUtils.isEmpty(children)) {
            ++n;    // 下一层需要打印的 \t 数量
            for (String cPath : children) {
                for (int i = 0; i < n; i++) {
                    sb.append("\t");
                }
                if (n > 0){
                    sb.append("├── ");
                }
                String cFullPath = (path.equals("/") ? "" : path) + "/" + cPath;
                sb.append(cPath);
                sb.append("(");
                sb.append(getPathValue(cFullPath));
                sb.append(")");
                sb.append("[");
                sb.append(ephemeralOrPersistent(cFullPath));
                sb.append("]\n");
                printNode(cFullPath, n, sb);
            }
        }
    }

    /**
     * 获取节点value
     * @param path
     * @return
     * @throws Exception
     */
    public String getPathValue(String path) throws Exception {
        String v = new String(client.getData().forPath(path));
        return StringUtils.isEmpty(v) ? null : v;
    }

    /**
     * 设置节点value
     * @param path
     * @param value
     * @throws Exception
     */
    public void setPathValue(String path, String value) throws Exception {
        client.setData().forPath(path, value.getBytes());
    }

    /**
     * 判断节点类型
     * @param childPath
     * @return
     */
    private String ephemeralOrPersistent(String childPath) throws Exception {
        Stat stat = client.checkExists().forPath(childPath);
        return stat.getEphemeralOwner() > 0 ? "临时节点" : "永久节点";
    }

    /**
     * 添加子节点监听
     * @param listener
     * @param path
     * @throws Exception
     */
    public void addWatcher(PathChildrenCacheListener listener, String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start();
        cache.getListenable().addListener(listener);
        PATH_LISTENER_MAP.put(path, cache);
    }

    /**
     * 节点监听
     * @param listener
     * @param path
     * @throws Exception
     */
    public void addWatcher(NodeCacheListener listener, String path) throws Exception {
        NodeCache cache = new NodeCache(client, path);
        cache.start();
        cache.getListenable().addListener(listener);
        NODE_LISTENER_MAP.put(path, cache);
    }

    /**
     * 树监听
     * @param listener
     * @param path
     * @throws Exception
     */
    public void addWatcher(TreeCacheListener listener, String path) throws Exception {
        TreeCache cache = new TreeCache(client, path);
        cache.start();
        cache.getListenable().addListener(listener);
        TREE_LISTENER_MAP.put(path, cache);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
