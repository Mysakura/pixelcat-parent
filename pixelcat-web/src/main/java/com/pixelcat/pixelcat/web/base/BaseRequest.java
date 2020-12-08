package com.pixelcat.pixelcat.web.base;

import lombok.Data;

@Data
public class BaseRequest {

    private Integer page;
    private Integer pageSize;
    private Integer limitStart;
    private Integer limitEnd;

    public Integer getLimitStart() {
        return pageSize * (page - 1);
    }

    public Integer getLimitEnd() {
        return pageSize;
    }
}
