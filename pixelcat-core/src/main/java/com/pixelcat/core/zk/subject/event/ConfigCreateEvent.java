package com.pixelcat.core.zk.subject.event;


import java.util.Map;

public class ConfigCreateEvent extends BaseConfigEvent {


    public ConfigCreateEvent(String projectId, String envId, String namespace, Map<String, Object> dataMap) {
        super(projectId, envId, namespace, dataMap);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.CREATE;
    }




}
