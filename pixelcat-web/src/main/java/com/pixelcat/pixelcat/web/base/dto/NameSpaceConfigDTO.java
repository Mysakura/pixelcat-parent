package com.pixelcat.pixelcat.web.base.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class NameSpaceConfigDTO {

    private Long id;

    private String key;

    private String value;

    private Long namespaceId;

    private String username;

    private Date updateTime;

    private String updateTimeStr;

    private Integer deleteFlag;

    public String getUpdateTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
    }
}
