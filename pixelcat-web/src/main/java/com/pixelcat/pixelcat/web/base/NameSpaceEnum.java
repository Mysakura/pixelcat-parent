package com.pixelcat.pixelcat.web.base;

public enum NameSpaceEnum {
    PROJECT(1),
    ENV(2),
    NAME_SPACE(3),
    ;

    private Integer code;

    NameSpaceEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
