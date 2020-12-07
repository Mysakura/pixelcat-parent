package com.pixelcat.core.zk.subject;

import com.pixelcat.core.zk.subject.event.BaseConfigEvent;

public interface ConfigChangeListener {

    void action(BaseConfigEvent event);
}
