package com.pixelcat.core.zk.subject;

import com.pixelcat.core.zk.subject.event.BaseConfigEvent;

public interface ConfigSubject {

    /**
     * 添加配置监听
     * @param listener
     */
    void addConfigChangeListener(ConfigChangeListener listener);

    void publishEvent(BaseConfigEvent event);

}
