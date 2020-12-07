package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.db.executor.Executor;
import com.pixelcat.core.db.executor.ExecutorFactory;
import com.pixelcat.core.zk.handle.ConfigHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 初始化配置中心
 */
@Slf4j
public class ConfigCenterInit implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ConfigHandler configHandler;
    private ExecutorFactory executorFactory;

    @Autowired
    public ConfigCenterInit(ConfigHandler configHandler, ExecutorFactory executorFactory) {
        this.configHandler = configHandler;
        this.executorFactory = executorFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 1. 读取数据库
        List<NameSpace> nameSpaces = readFromDb();
        // 2. 初始化zk节点
        initZkNode(nameSpaces);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 读取数据库这部分可以由子类实现，在web项目中
     * @return
     */
    private List<NameSpace> readFromDb(){
        Executor executor = executorFactory.newExecutor();
        List<NameSpace> list = executor.getList(NameSpace.class, "select * from namespace where type = 3", null);
        executor.close();
        return list;
    }

    private void initZkNode(List<NameSpace> nameSpaces){
        if (CollectionUtils.isEmpty(nameSpaces)){
            log.warn("没有需要初始化的节点，请检查数据库！");
        }else {
            nameSpaces.forEach(e -> {
                String node = e.getProjectName() + "/" + e.getEnvName() + "/" + e.getName();
                configHandler.createEphemeralPath(node, "INIT");
            });
        }
    }
}
