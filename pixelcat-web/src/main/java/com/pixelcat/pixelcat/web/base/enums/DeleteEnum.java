package com.pixelcat.pixelcat.web.base.enums;

public enum DeleteEnum {
    NO(1, "未删除"),
    YES(2, "已删除");


    private Integer code;
    private String name;

    DeleteEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
