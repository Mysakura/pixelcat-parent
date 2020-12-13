package com.pixelcat.core.config.event;

import com.pixelcat.core.zk.handle.ConfigHandler;
import org.springframework.context.ApplicationEvent;

public class PixelCatConfigEvent extends ApplicationEvent {

    private final String projectId;
    private final String envId;
    private final String namespaceId;

    public PixelCatConfigEvent(ConfigHandler source, String projectId, String envId, String namespaceId) {
        super(source);
        this.projectId = projectId;
        this.envId = envId;
        this.namespaceId = namespaceId;
    }

    @Override
    public ConfigHandler getSource() {
        return (ConfigHandler) super.getSource();
    }

    public String getProjectId() {
        return projectId;
    }

    public String getEnvId() {
        return envId;
    }

    public String getNamespaceId() {
        return namespaceId;
    }
}
