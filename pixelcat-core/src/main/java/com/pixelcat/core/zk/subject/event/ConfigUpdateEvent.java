package com.pixelcat.core.zk.subject.event;


public class ConfigUpdateEvent extends BaseConfigEvent {

    public ConfigUpdateEvent(String path, String value) {
        super(path, value);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.UPDATE;
    }

}
