package com.pixelcat.core.zk.subject.event;


public class ConfigCreateEvent extends BaseConfigEvent {


    public ConfigCreateEvent(String path, String value) {
        super(path, value);
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.CREATE;
    }




}
