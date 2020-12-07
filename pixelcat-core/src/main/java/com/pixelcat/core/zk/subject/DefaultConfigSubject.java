package com.pixelcat.core.zk.subject;

import com.pixelcat.core.zk.subject.event.BaseConfigEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 配置主题
 */
@Slf4j
public class DefaultConfigSubject implements ConfigSubject {

    private static final ConcurrentLinkedQueue<ConfigChangeListener> LISTENERS = new ConcurrentLinkedQueue<>();

    @Override
    public void addConfigChangeListener(ConfigChangeListener listener) {
        LISTENERS.add(listener);
    }

    @Override
    public void publishEvent(BaseConfigEvent event) {
        this.notifyListeners(event);
    }

    private void notifyListeners(BaseConfigEvent event){
        for (ConfigChangeListener l : LISTENERS){
            l.action(event);
        }
    }
}
