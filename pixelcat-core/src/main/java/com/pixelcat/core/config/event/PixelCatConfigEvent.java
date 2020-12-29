package com.pixelcat.core.config.event;

import com.pixelcat.core.zk.handle.ConfigNodeHandler;
import org.springframework.context.ApplicationEvent;

/**
 * 不利用ApplicationListener来传播事件了
 */
@Deprecated
public class PixelCatConfigEvent extends ApplicationEvent {

    private final String projectId;
    private final String envId;
    private final String namespaceId;

    public PixelCatConfigEvent(ConfigNodeHandler source, String projectId, String envId, String namespaceId) {
        super(source);
        this.projectId = projectId;
        this.envId = envId;
        this.namespaceId = namespaceId;
    }

    @Override
    public ConfigNodeHandler getSource() {
        return (ConfigNodeHandler) super.getSource();
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
