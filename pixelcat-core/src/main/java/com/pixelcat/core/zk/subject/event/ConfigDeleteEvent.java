package com.pixelcat.core.zk.subject.event;


import java.util.Map;

public class ConfigDeleteEvent extends BaseConfigEvent {

    public ConfigDeleteEvent(String projectId, String envId, String namespace, Map<String, Object> dataMap) {
        super(projectId, envId, namespace, dataMap);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.DELETE;
    }

}
