package com.pixelcat.core.zk.subject.event;

public abstract class BaseConfigEvent implements ConfigEvent {

    private String path;
    private String value;

    public BaseConfigEvent(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public String path() {
        return path;
    }

    public String value() {
        return value;
    }

}
