package com.pixelcat.pixelcat.web.base.enums;

public enum StatusEnum {
    ADD(1, "新增"),
    UPDATE(2, "更新"),
    DELETE(3, "删除");


    private Integer code;
    private String name;

    StatusEnum(Integer code, String name) {
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
