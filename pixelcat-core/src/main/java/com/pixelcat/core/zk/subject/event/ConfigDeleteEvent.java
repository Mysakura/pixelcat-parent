package com.pixelcat.core.zk.subject.event;


public class ConfigDeleteEvent extends BaseConfigEvent {

    public ConfigDeleteEvent(String path, String value) {
        super(path, value);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.DELETE;
    }

}
