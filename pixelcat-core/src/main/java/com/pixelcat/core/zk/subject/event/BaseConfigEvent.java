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

    @Override
    public String toString() {
        return "BaseConfigEvent{" +
                "projectId='" + projectId + '\'' +
                ", envId='" + envId + '\'' +
                ", namespace='" + namespace + '\'' +
                ", dataMap=" + parseMap(dataMap) +
                '}';
    }

    private String parseMap(Map<String, Object> dataMap){
        if (dataMap != null && dataMap.size() > 0){
            StringBuilder sb = new StringBuilder();
            dataMap.forEach((k,v) -> {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append(",");
            });
            return sb.toString().substring(0, sb.lastIndexOf(","));
        }
        return null;
    }
}
