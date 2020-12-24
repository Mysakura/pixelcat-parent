package com.pixelcat.core.zk.subject.event;


import java.util.Map;

public class ConfigUpdateEvent extends BaseConfigEvent {

    public ConfigUpdateEvent(String projectId, String envId, String namespace, Map<String, Object> dataMap) {
        super(projectId, envId, namespace, dataMap);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.UPDATE;
    }

}
