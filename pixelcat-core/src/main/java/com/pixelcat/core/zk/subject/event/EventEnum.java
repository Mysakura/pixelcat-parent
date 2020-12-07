package com.pixelcat.core.zk.subject.event;

public enum EventEnum {
    CREATE(1),
    UPDATE(2),
    DELETE(3),
    ;

    EventEnum(int eventType) {
        this.eventType = eventType;
    }

    private int eventType;

    public int getEventType() {
        return eventType;
    }
}
