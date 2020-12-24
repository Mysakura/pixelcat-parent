package com.pixelcat.core.zk.subject.event;

import java.util.Map;

public abstract class BaseConfigEvent implements ConfigEvent {

    private String projectId;
    private String envId;
    private String namespace;
    private Map<String, Object> dataMap;

    public BaseConfigEvent(String projectId, String envId, String namespace, Map<String, Object> dataMap) {
        this.projectId = projectId;
        this.envId = envId;
        this.namespace = namespace;
        this.dataMap = dataMap;
    }

    public String projectId() {
        return projectId;
    }

    public String envId() {
        return envId;
    }

    public String namespace() {
        return namespace;
    }

    public Map<String, Object> dataMap() {
        return dataMap;
    }
}
