package com.pixelcat.core.zk.subject;

import com.pixelcat.core.zk.subject.event.BaseConfigEvent;

/**
 * 客户端监听配置变化，实现此接口
 */
public interface ConfigChangeListener {

    void action(BaseConfigEvent event);
}
